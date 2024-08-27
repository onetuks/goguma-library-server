package com.onetuks.librarydomain.review.repository;

import com.onetuks.librarydomain.review.model.ReviewPick;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewPickRepository {

  ReviewPick create(ReviewPick reviewPick);

  ReviewPick read(long reviewPickId);

  Page<ReviewPick> readAll(long memberId, Pageable pageable);

  ReviewPick read(long memberId, long reviewId);

  Long readCount(long loginId, long reviewId);

  void delete(long reviewPickId);
}
