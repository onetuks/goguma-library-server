package com.onetuks.dbstorage.review.repository;

import com.onetuks.dbstorage.review.entity.ReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewEntityJpaRepository extends JpaRepository<ReviewEntity, Long> {

  Page<ReviewEntity> findAllByMemberEntityMemberId(long memberId, Pageable pageable);
}
