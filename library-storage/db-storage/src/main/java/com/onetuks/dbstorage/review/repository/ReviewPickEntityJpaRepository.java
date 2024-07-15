package com.onetuks.dbstorage.review.repository;

import com.onetuks.dbstorage.review.entity.ReviewPickEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewPickEntityJpaRepository extends JpaRepository<ReviewPickEntity, Long> {

  Page<ReviewPickEntity> findAllByMemberEntityMemberId(long memberId, Pageable pageable);

  boolean existsByMemberEntityMemberIdAndReviewEntityReviewId(long memberId, long reviewId);
}
