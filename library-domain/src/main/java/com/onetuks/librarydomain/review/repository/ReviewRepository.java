package com.onetuks.librarydomain.review.repository;

import com.onetuks.librarydomain.review.model.Review;
import com.onetuks.libraryobject.enums.SortBy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository {

  Review create(Review review);

  Review read(long reviewId);

  Page<Review> readAll(SortBy sortBy, Pageable pageable);

  Page<Review> readAll(long bookId, SortBy sortBy, Pageable pageable);

  Page<Review> readAll(long memberId, Pageable pageable);

  Review update(Review review);

  void delete(long reviewId);
}
