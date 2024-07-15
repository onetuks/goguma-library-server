package com.onetuks.libraryapi.review.dto.response;

import com.onetuks.librarydomain.review.model.ReviewPick;
import org.springframework.data.domain.Page;

public record ReviewPickResponse(long reviewPickId, long memberId, long reviewId) {

  public static ReviewPickResponse from(ReviewPick reviewPick) {
    return new ReviewPickResponse(
        reviewPick.reviewPickId(), reviewPick.member().memberId(), reviewPick.review().reviewId());
  }

  public record ReviewPickResponses(Page<ReviewPickResponse> responses) {

    public static ReviewPickResponses from(Page<ReviewPick> results) {
      return new ReviewPickResponses(results.map(ReviewPickResponse::from));
    }
  }
}
