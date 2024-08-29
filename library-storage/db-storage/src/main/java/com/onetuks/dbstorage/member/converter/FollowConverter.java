package com.onetuks.dbstorage.member.converter;

import com.onetuks.dbstorage.member.entity.FollowEntity;
import com.onetuks.librarydomain.member.model.Follow;
import org.springframework.stereotype.Component;

@Component
public class FollowConverter {

  private final MemberConverter memberConverter;

  public FollowConverter(MemberConverter memberConverter) {
    this.memberConverter = memberConverter;
  }

  public FollowEntity toEntity(Follow model) {
    return new FollowEntity(
        model.followId(),
        memberConverter.toEntity(model.follower()),
        memberConverter.toEntity(model.followee()));
  }

  public Follow toModel(FollowEntity entity) {
    if (entity == null) {
      return null;
    }

    return new Follow(
        entity.getFollowId(),
        memberConverter.toModel(entity.getFollower()),
        memberConverter.toModel(entity.getFollowee()));
  }
}
