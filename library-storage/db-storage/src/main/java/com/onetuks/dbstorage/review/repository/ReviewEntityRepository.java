package com.onetuks.dbstorage.review.repository;

import com.onetuks.dbstorage.book.converter.BookConverter;
import com.onetuks.dbstorage.common.DeletionRepository;
import com.onetuks.dbstorage.member.converter.MemberConverter;
import com.onetuks.dbstorage.review.converter.ReviewConverter;
import com.onetuks.librarydomain.book.model.Book;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.review.model.Review;
import com.onetuks.librarydomain.review.repository.ReviewRepository;
import com.onetuks.libraryobject.enums.SortBy;
import com.onetuks.libraryobject.exception.NoSuchEntityException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewEntityRepository implements ReviewRepository {

  private final ReviewEntityJpaRepository repository;
  private final ReviewEntityJpaQueryDslRepository qDslRepository;
  private final DeletionRepository deletionRepository;
  private final ReviewConverter converter;
  private final BookConverter bookConverter;
  private final MemberConverter memberConverter;

  public ReviewEntityRepository(
      ReviewEntityJpaRepository repository,
      ReviewEntityJpaQueryDslRepository qDslRepository,
      DeletionRepository deletionRepository,
      ReviewConverter converter,
      BookConverter bookConverter,
      MemberConverter memberConverter) {
    this.repository = repository;
    this.qDslRepository = qDslRepository;
    this.deletionRepository = deletionRepository;
    this.converter = converter;
    this.bookConverter = bookConverter;
    this.memberConverter = memberConverter;
  }

  @Override
  public Review create(Review review) {
    return converter.toModel(repository.save(converter.toEntity(review)));
  }

  @Override
  public Review read(long reviewId) {
    return converter.toModel(
        repository
            .findById(reviewId)
            .orElseThrow(() -> new NoSuchEntityException("존재하지 않는 서평입니다.")));
  }

  @Override
  public Review readWithLock(long reviewId) {
    return converter.toModel(
        repository
            .findByReviewId(reviewId)
            .orElseThrow(() -> new NoSuchEntityException("존재하지 않는 사람입니다.")));
  }

  @Override
  public Page<Review> readAll(SortBy sortBy, Pageable pageable) {
    return sortBy == SortBy.LATEST
        ? qDslRepository.findAllOrderByLatest(pageable).map(converter::toModel)
        : qDslRepository.findAllOrderByPickCountDesc(pageable).map(converter::toModel);
  }

  @Override
  public Page<Review> readAllByBook(long bookId, SortBy sortBy, Pageable pageable) {
    return qDslRepository.findAllByBookId(bookId, sortBy, pageable).map(converter::toModel);
  }

  @Override
  public Page<Review> readAllByMember(long memberId, SortBy sortBy, Pageable pageable) {
    return qDslRepository.findAllByMemberId(memberId, sortBy, pageable).map(converter::toModel);
  }

  @Override
  public Page<Review> readAll(List<Book> thisWeekInterestedCategoriesBooks, Pageable pageable) {
    return repository
        .findAllByBookEntityIn(
            bookConverter.toEntities(thisWeekInterestedCategoriesBooks), pageable)
        .map(converter::toModel);
  }

  @Override
  public Page<Review> readAllWeeklyMostPicked(List<Book> thisWeekFeaturedBooks) {
    return repository
        .findAllByBookEntityInOrderByPickCountDesc(
            bookConverter.toEntities(thisWeekFeaturedBooks),
            PageRequest.of(0, BEST_QUALITY_MEMBER_COUNT))
        .map(converter::toModel);
  }

  @Override
  public Page<Member> readAllWeeklyMostWrite(List<Book> thisWeekFeaturedBooks) {
    return repository
        .findAllByWeeklyMostCountDesc(
            bookConverter.toEntities(thisWeekFeaturedBooks),
            PageRequest.of(0, BEST_QUANTITY_MEMBER_COUNT))
        .map(memberConverter::toModel);
  }

  @Override
  public Review update(Review review) {
    return converter.toModel(repository.save(converter.toEntity(review)));
  }

  @Override
  public void delete(long reviewId) {
    deletionRepository.deleteReview(reviewId);
  }
}
