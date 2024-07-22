package com.onetuks.librarydomain;

import com.onetuks.librarydomain.member.model.FollowShip;
import com.onetuks.librarydomain.member.model.Member;

public class FollowShipFixture {

  public static FollowShip create(Long followShipId, Member follower, Member followee) {
    return new FollowShip(followShipId, follower, followee);
  }
}
