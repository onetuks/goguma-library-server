package com.onetuks.librarydomain;

import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.review.model.Review;
import com.onetuks.librarydomain.review.model.ReviewPick;

public class ReviewPickFixture {

  public static ReviewPick create(Long reviewPickId, Member member, Review review) {
    return new ReviewPick(reviewPickId, member, review);
  }
}
