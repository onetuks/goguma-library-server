package com.onetuks.librarydomain.review.service;

import com.onetuks.librarydomain.book.model.Book;
import com.onetuks.librarydomain.book.repository.BookRepository;
import com.onetuks.librarydomain.global.point.producer.PointEventProducer;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.member.repository.MemberRepository;
import com.onetuks.librarydomain.review.model.Review;
import com.onetuks.librarydomain.review.repository.ReviewRepository;
import com.onetuks.librarydomain.review.service.dto.param.ReviewParam;
import com.onetuks.librarydomain.weekly.model.WeeklyFeaturedBook;
import com.onetuks.librarydomain.weekly.repository.WeeklyFeaturedBookRepository;
import com.onetuks.libraryobject.enums.CacheName;
import com.onetuks.libraryobject.enums.Category;
import com.onetuks.libraryobject.enums.SortBy;
import com.onetuks.libraryobject.exception.ApiAccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {

  private final ReviewRepository reviewRepository;
  private final MemberRepository memberRepository;
  private final BookRepository bookRepository;
  private final WeeklyFeaturedBookRepository weeklyFeaturedBookRepository;

  private final PointEventProducer pointEventProducer;

  public ReviewService(
      ReviewRepository reviewRepository,
      MemberRepository memberRepository,
      BookRepository bookRepository,
      WeeklyFeaturedBookRepository weeklyFeaturedBookRepository,
      PointEventProducer pointEventProducer) {
    this.reviewRepository = reviewRepository;
    this.memberRepository = memberRepository;
    this.bookRepository = bookRepository;
    this.weeklyFeaturedBookRepository = weeklyFeaturedBookRepository;
    this.pointEventProducer = pointEventProducer;
  }

  @Transactional
  public Review register(long loginId, long bookId, ReviewParam param) {
    Member member = memberRepository.read(loginId);
    Book book = bookRepository.read(bookId);

    Member updateMember =
        memberRepository.update(
            member
                .increaseReviewCategoryStatics(book.categories())
                .creditBadgeForReviewRegistration());

    boolean isFeaturedBook =
        weeklyFeaturedBookRepository.readAllForThisWeek().getContent().stream()
            .map(WeeklyFeaturedBook::book)
            .anyMatch(featuredBook -> Objects.equals(featuredBook.bookId(), book.bookId()));
    pointEventProducer.creditPointForReviewRegistration(member.memberId(), isFeaturedBook);

    return reviewRepository.create(
        new Review(updateMember, book, param.reviewTitle(), param.reviewContent()));
  }

  @Transactional
  public Review edit(long loginId, long reviewId, ReviewParam param) {
    Review review = reviewRepository.read(reviewId);

    checkAuthentication(loginId, review);

    return reviewRepository.update(
        new Review(
            review.reviewId(),
            review.member(),
            review.book(),
            param.reviewTitle(),
            param.reviewContent(),
            review.pickCount(),
            review.createdAt(),
            LocalDateTime.now()));
  }

  @Transactional
  public void remove(long loginId, long reviewId) {
    Review review = reviewRepository.read(reviewId);

    checkAuthentication(loginId, review);

    memberRepository.update(
        review.member().decreaseReviewCategoryStatics(review.book().categories()));
    pointEventProducer.debitPointForReviewRemoval(loginId);

    reviewRepository.delete(reviewId);
  }

  @Transactional(readOnly = true)
  public Review search(long reviewId) {
    return reviewRepository.read(reviewId);
  }

  @Cacheable(
      value = CacheName.REVIEW_FEED,
      key =
          "#sortBy.ordinal()" + "-" + "#pageable.getPageNumber()" + "-" + "#pageable.getPageSize()")
  @Transactional(readOnly = true)
  public Slice<Review> searchAll(SortBy sortBy, Pageable pageable) {
    return reviewRepository.readAll(sortBy, pageable);
  }

  @Transactional(readOnly = true)
  public Page<Review> searchAllOfBook(long bookId, SortBy sortBy, Pageable pageable) {
    return reviewRepository.readAllByBook(bookId, sortBy, pageable);
  }

  @Transactional(readOnly = true)
  public Page<Review> searchAllOfMember(long memberId, SortBy sortBy, Pageable pageable) {
    return reviewRepository.readAllByMember(memberId, sortBy, pageable);
  }

  @Transactional(readOnly = true)
  public Page<Review> searchAllWithInterestedCategories(long memberId, Pageable pageable) {
    Set<Category> interestedCategories = memberRepository.read(memberId).interestedCategories();
    List<Book> thisWeekInterestedCategoriesBooks =
        weeklyFeaturedBookRepository.readAllForThisWeek().getContent().stream()
            .map(WeeklyFeaturedBook::book)
            .filter(book -> book.categories().stream().anyMatch(interestedCategories::contains))
            .toList();

    return reviewRepository.readAll(thisWeekInterestedCategoriesBooks, pageable);
  }

  private void checkAuthentication(long loginId, Review review) {
    if (review.member().memberId() != loginId) {
      throw new ApiAccessDeniedException("해당 서평에 대한 권한이 없는 멤버입니다.");
    }
  }
}
