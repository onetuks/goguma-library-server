package com.onetuks.librarydomain.review.repository;

import com.onetuks.librarydomain.review.model.Review;
import com.onetuks.libraryobject.enums.ReviewSortBy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository {

  Review create(Review review);

  Review read(long reviewId);

  Page<Review> readAll(ReviewSortBy reviewSortBy, Pageable pageable);

  Review update(Review review);

  void delete(long reviewId);
}
