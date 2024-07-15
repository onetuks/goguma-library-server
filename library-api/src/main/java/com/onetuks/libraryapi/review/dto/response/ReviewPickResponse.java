package com.onetuks.libraryapi.review.dto.response;

import com.onetuks.librarydomain.review.model.ReviewPick;

public record ReviewPickResponse(long reviewPickId, long memberId, long reviewId) {

  public static ReviewPickResponse from(ReviewPick reviewPick) {
    return new ReviewPickResponse(
        reviewPick.reviewPickId(), reviewPick.member().memberId(), reviewPick.review().reviewId());
  }
}
