package com.onetuks.dbstorage.review.repository;

import com.onetuks.dbstorage.review.converter.ReviewConverter;
import com.onetuks.librarydomain.review.model.Review;
import com.onetuks.librarydomain.review.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewEntityRepository implements ReviewRepository {

  private final ReviewEntityJpaRepository repository;
  private final ReviewConverter converter;

  public ReviewEntityRepository(ReviewEntityJpaRepository repository, ReviewConverter converter) {
    this.repository = repository;
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
  public Review update(Review review) {
    return converter.toDomain(repository.save(converter.toEntity(review)));
  }

  @Override
  public void delete(long reviewId) {
    repository.deleteById(reviewId);
  }
}
