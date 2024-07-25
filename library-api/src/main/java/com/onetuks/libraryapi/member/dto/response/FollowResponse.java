package com.onetuks.libraryapi.member.dto.response;

import com.onetuks.librarydomain.member.model.Follow;

public record FollowResponse(long followId, long followerId, long followeeId) {

  public static FollowResponse from(Follow model) {
    return new FollowResponse(
        model.followId(), model.follower().memberId(), model.followee().memberId());
  }
}
