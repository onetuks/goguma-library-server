package com.onetuks.librarydomain.review.service;

import static com.onetuks.librarydomain.member.repository.PointRepository.REVIEW_BASE_REGISTRATION_POINT;

import com.onetuks.librarydomain.book.model.Book;
import com.onetuks.librarydomain.book.repository.BookRepository;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.member.repository.MemberRepository;
import com.onetuks.librarydomain.member.repository.PointRepository;
import com.onetuks.librarydomain.review.model.Review;
import com.onetuks.librarydomain.review.repository.ReviewRepository;
import com.onetuks.librarydomain.review.service.dto.param.ReviewParam;
import com.onetuks.libraryobject.enums.ReviewSortBy;
import com.onetuks.libraryobject.exception.ApiAccessDeniedException;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {

  private final ReviewRepository reviewRepository;
  private final MemberRepository memberRepository;
  private final BookRepository bookRepository;
  private final PointRepository pointRepository;

  public ReviewService(
      ReviewRepository reviewRepository,
      MemberRepository memberRepository,
      BookRepository bookRepository,
      PointRepository pointRepository) {
    this.reviewRepository = reviewRepository;
    this.memberRepository = memberRepository;
    this.bookRepository = bookRepository;
    this.pointRepository = pointRepository;
  }

  @Transactional
  public Review register(long loginId, long bookId, ReviewParam param) {
    // todo 서평 등록 시 포인트 지급 (하루 5건 이하로 포인트 수령 가능)
    Member member = memberRepository.read(loginId);
    Book book = bookRepository.read(bookId);

    Member updateMember =
        memberRepository.update(member.increaseReviewCategoryStatics(book.categories()));

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
    pointRepository.debitPoints(loginId, REVIEW_BASE_REGISTRATION_POINT);

    reviewRepository.delete(reviewId);
  }

  @Transactional(readOnly = true)
  public Review search(long reviewId) {
    return reviewRepository.read(reviewId);
  }

  @Transactional(readOnly = true)
  public Page<Review> searchAll(ReviewSortBy reviewSortBy, Pageable pageable) {
    return reviewRepository.readAll(reviewSortBy, pageable);
  }

  private void checkAuthentication(long loginId, Review review) {
    if (review.member().memberId() != loginId) {
      throw new ApiAccessDeniedException("해당 서평에 대한 권한이 없는 멤버입니다.");
    }
  }
}
