package com.onetuks.librarydomain.review.repository;

import com.onetuks.librarydomain.review.model.Review;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository {

  Review create(Review review);

  Review read(long reviewId);

  Review update(Review review);

  void delete(long reviewId);
}
