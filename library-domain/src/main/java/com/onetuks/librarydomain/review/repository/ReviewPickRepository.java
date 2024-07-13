package com.onetuks.librarydomain.review.repository;

import com.onetuks.librarydomain.review.model.ReviewPick;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewPickRepository {

  ReviewPick create(ReviewPick reviewPick);

  ReviewPick read(long reviewPickId);

  boolean read(long memberId, long reviewId);

  void delete(long reviewPickId);
}
