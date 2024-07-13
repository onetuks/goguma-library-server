package com.onetuks.dbstorage.review.repository;

import com.onetuks.dbstorage.review.entity.ReviewPickEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewPickEntityJpaRepository extends JpaRepository<ReviewPickEntity, Long> {

  boolean existsByMemberEntityMemberIdAndReviewEntityReviewId(long memberId, long reviewId);
}
