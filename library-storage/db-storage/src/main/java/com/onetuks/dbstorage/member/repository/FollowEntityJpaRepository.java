package com.onetuks.dbstorage.member.repository;

import com.onetuks.dbstorage.member.entity.FollowEntity;
import com.onetuks.dbstorage.member.entity.MemberEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowEntityJpaRepository extends JpaRepository<FollowEntity, Long> {

  Optional<FollowEntity> findByFollowerMemberIdAndFolloweeMemberId(
      long followerMemberId, long followeeMemberId);

  Page<FollowEntity> findAllByFolloweeMemberId(long followeeId, Pageable pageable);

  Page<FollowEntity> findAllByFollowerMemberId(long followerId, Pageable pageable);

  void deleteAllByFollower(MemberEntity memberEntity);

  void deleteAllByFollowee(MemberEntity memberEntity);
}
