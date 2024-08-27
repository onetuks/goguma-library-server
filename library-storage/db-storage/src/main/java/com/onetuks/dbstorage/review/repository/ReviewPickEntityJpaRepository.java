package com.onetuks.dbstorage.review.repository;

import com.onetuks.dbstorage.review.entity.ReviewPickEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewPickEntityJpaRepository extends JpaRepository<ReviewPickEntity, Long> {

  Page<ReviewPickEntity> findAllByMemberEntityMemberId(long memberId, Pageable pageable);

  Optional<ReviewPickEntity> findByMemberEntityMemberIdAndReviewEntityReviewId(
      long memberId, long reviewId);

  Long countByMemberEntityMemberIdAndReviewEntityReviewId(long loginId, long reviewId);
}
