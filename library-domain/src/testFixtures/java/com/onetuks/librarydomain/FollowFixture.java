package com.onetuks.librarydomain;

import com.onetuks.librarydomain.member.model.Follow;
import com.onetuks.librarydomain.member.model.Member;

public class FollowFixture {

  public static Follow create(Long followId, Member follower, Member followee) {
    return new Follow(followId, follower, followee);
  }
}
