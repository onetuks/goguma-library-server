package com.onetuks.dbstorage.review.repository;

import com.onetuks.dbstorage.review.converter.ReviewConverter;
import com.onetuks.librarydomain.review.model.Review;
import com.onetuks.librarydomain.review.repository.ReviewRepository;
import com.onetuks.libraryobject.enums.SortBy;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewEntityRepository implements ReviewRepository {

  private final ReviewEntityJpaRepository repository;
  private final ReviewEntityJpaQueryDslRepository qDslRepository;
  private final ReviewConverter converter;

  public ReviewEntityRepository(
      ReviewEntityJpaRepository repository,
      ReviewEntityJpaQueryDslRepository qDslRepository,
      ReviewConverter converter) {
    this.repository = repository;
    this.qDslRepository = qDslRepository;
    this.converter = converter;
  }

  @Override
  public Review create(Review review) {
    return converter.toDomain(repository.save(converter.toEntity(review)));
  }

  @Override
  public Review read(long reviewId) {
    return converter.toDomain(
        repository
            .findById(reviewId)
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 서평입니다.")));
  }

  @Override
  public Page<Review> readAll(SortBy sortBy, Pageable pageable) {
    return qDslRepository.findAll(sortBy, pageable).map(converter::toDomain);
  }

  @Override
  public Page<Review> readAll(long bookId, SortBy sortBy, Pageable pageable) {
    return qDslRepository.findAll(bookId, sortBy, pageable).map(converter::toDomain);
  }

  @Override
  public Page<Review> readAll(long memberId, Pageable pageable) {
    return repository.findAllByMemberEntityMemberId(memberId, pageable).map(converter::toDomain);
  }

  @Override
  public Review update(Review review) {
    return converter.toDomain(repository.save(converter.toEntity(review)));
  }

  @Override
  public void delete(long reviewId) {
    repository.deleteById(reviewId);
  }
}
