package com.onetuks.dbstorage.member.converter;

import com.onetuks.dbstorage.member.entity.FollowShipEntity;
import com.onetuks.librarydomain.member.model.FollowShip;
import org.springframework.stereotype.Component;

@Component
public class FollowShipConverter {

  private final MemberConverter memberConverter;

  public FollowShipConverter(MemberConverter memberConverter) {
    this.memberConverter = memberConverter;
  }

  public FollowShipEntity toEntity(FollowShip model) {
    return new FollowShipEntity(
        model.followShipId(),
        memberConverter.toEntity(model.follower()),
        memberConverter.toEntity(model.followee()));
  }

  public FollowShip toModel(FollowShipEntity entity) {
    return new FollowShip(
        entity.getFollowShipId(),
        memberConverter.toModel(entity.getFollower()),
        memberConverter.toModel(entity.getFollowee()));
  }
}
