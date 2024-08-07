package com.onetuks.dbstorage.member.repository;

import com.onetuks.dbstorage.member.entity.FollowEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowEntityJpaRepository extends JpaRepository<FollowEntity, Long> {

  boolean existsByFollowerMemberIdAndFolloweeMemberId(long followerMemberId, long followeeMemberId);

  Page<FollowEntity> findAllByFolloweeMemberId(long followeeId, Pageable pageable);

  Page<FollowEntity> findAllByFollowerMemberId(long followerId, Pageable pageable);
}
