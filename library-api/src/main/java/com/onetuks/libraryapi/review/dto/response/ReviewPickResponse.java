package com.onetuks.libraryapi.review.dto.response;

import com.onetuks.librarydomain.review.model.ReviewPick;

public record ReviewPickResponse(long reviewPickId, long memberId, long reviewId) {

  public static ReviewPickResponse from(ReviewPick model) {
    if (model == null) {
      return null;
    }

    return new ReviewPickResponse(
        model.reviewPickId(), model.member().memberId(), model.review().reviewId());
  }
}
