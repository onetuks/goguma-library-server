package com.onetuks.dbstorage.member.repository;

import com.onetuks.dbstorage.member.entity.FollowShipEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowShipEntityJpaRepository extends JpaRepository<FollowShipEntity, Long> {

  boolean existsByFollowerMemberIdAndFolloweeMemberId(long followerMemberId, long followeeMemberId);
}
