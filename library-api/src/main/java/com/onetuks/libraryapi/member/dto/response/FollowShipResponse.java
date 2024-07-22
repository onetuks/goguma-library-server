package com.onetuks.libraryapi.member.dto.response;

import com.onetuks.librarydomain.member.model.FollowShip;

public record FollowShipResponse(long followShipId, long followerId, long followeeId) {

  public static FollowShipResponse from(FollowShip model) {
    return new FollowShipResponse(
        model.followShipId(), model.follower().memberId(), model.followee().memberId());
  }
}
