package com.onetuks.dbstorage.review.repository;

import com.onetuks.dbstorage.review.converter.ReviewPickConverter;
import com.onetuks.librarydomain.review.model.ReviewPick;
import com.onetuks.librarydomain.review.repository.ReviewPickRepository;
import com.onetuks.libraryobject.exception.NoSuchEntityException;
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
    return converter.toModel(repository.save(converter.toEntity(reviewPick)));
  }

  @Override
  public ReviewPick read(long reviewPickId) {
    return converter.toModel(
        repository
            .findById(reviewPickId)
            .orElseThrow(() -> new NoSuchEntityException("존재하지 않는 서평픽입니다.")));
  }

  @Override
  public Page<ReviewPick> readAll(long memberId, Pageable pageable) {
    return repository.findAllByMemberEntityMemberId(memberId, pageable).map(converter::toModel);
  }

  @Override
  public ReviewPick read(long memberId, long reviewId) {
    return converter.toModel(
        repository
            .findByMemberEntityMemberIdAndReviewEntityReviewId(memberId, reviewId)
            .orElseThrow(() -> new NoSuchEntityException("존재하지 않는 서평픽입니다.")));
  }

  @Override
  public void delete(long reviewPickId) {
    repository.deleteById(reviewPickId);
  }
}
