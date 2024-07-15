package com.onetuks.dbstorage.review.repository;

import com.onetuks.dbstorage.review.converter.ReviewPickConverter;
import com.onetuks.librarydomain.review.model.ReviewPick;
import com.onetuks.librarydomain.review.repository.ReviewPickRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewPickEntityRepository implements ReviewPickRepository {

  private final ReviewPickEntityJpaRepository repository;
  private final ReviewPickConverter converter;

  public ReviewPickEntityRepository(
      ReviewPickEntityJpaRepository repository, ReviewPickConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  @Override
  public ReviewPick create(ReviewPick reviewPick) {
    return converter.toDomain(repository.save(converter.toEntity(reviewPick)));
  }

  @Override
  public ReviewPick read(long reviewPickId) {
    return converter.toDomain(
        repository
            .findById(reviewPickId)
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 서평픽입니다.")));
  }

  @Override
  public Page<ReviewPick> readAll(long memberId, Pageable pageable) {
    return repository.findAllByMemberEntityMemberId(memberId, pageable).map(converter::toDomain);
  }

  @Override
  public boolean read(long memberId, long reviewId) {
    return repository.existsByMemberEntityMemberIdAndReviewEntityReviewId(memberId, reviewId);
  }

  @Override
  public void delete(long reviewPickId) {
    repository.deleteById(reviewPickId);
  }
}
