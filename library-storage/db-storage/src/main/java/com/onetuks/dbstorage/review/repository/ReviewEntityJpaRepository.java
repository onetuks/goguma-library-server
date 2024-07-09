package com.onetuks.dbstorage.review.repository;

import com.onetuks.dbstorage.review.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewEntityJpaRepository extends JpaRepository<ReviewEntity, Long> {}
