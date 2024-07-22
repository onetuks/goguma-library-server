package com.onetuks.dbstorage.member.repository;

import com.onetuks.dbstorage.member.entity.FollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowEntityJpaRepository extends JpaRepository<FollowEntity, Long> {

  boolean existsByFollowerMemberIdAndFolloweeMemberId(long followerMemberId, long followeeMemberId);
}
