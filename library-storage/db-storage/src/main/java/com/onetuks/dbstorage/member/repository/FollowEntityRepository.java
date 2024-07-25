package com.onetuks.dbstorage.member.repository;

import com.onetuks.dbstorage.member.converter.FollowConverter;
import com.onetuks.dbstorage.member.converter.MemberConverter;
import com.onetuks.dbstorage.member.entity.FollowEntity;
import com.onetuks.librarydomain.member.model.Follow;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.member.repository.FollowRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class FollowEntityRepository implements FollowRepository {

  private final FollowEntityJpaRepository repository;
  private final FollowConverter followConverter;
  private final MemberConverter memberConverter;

  public FollowEntityRepository(
      FollowEntityJpaRepository repository,
      FollowConverter followConverter,
      MemberConverter memberConverter) {
    this.repository = repository;
    this.followConverter = followConverter;
    this.memberConverter = memberConverter;
  }

  @Override
  public Follow create(Follow follow) {
    return followConverter.toModel(repository.save(followConverter.toEntity(follow)));
  }

  @Override
  public Follow read(long followId) {
    return followConverter.toModel(
        repository
            .findById(followId)
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 팔로우십입니다.")));
  }

  @Override
  public boolean readExistence(long followerId, long followeeId) {
    return repository.existsByFollowerMemberIdAndFolloweeMemberId(followerId, followeeId);
  }

  @Override
  public Page<Member> readAllFollowers(long memberId, Pageable pageable) {
    return repository
        .findAllByFolloweeMemberId(memberId, pageable)
        .map(FollowEntity::getFollower)
        .map(memberConverter::toModel);
  }

  @Override
  public Page<Member> readAllFollowings(long memberId, Pageable pageable) {
    return repository
        .findAllByFollowerMemberId(memberId, pageable)
        .map(FollowEntity::getFollowee)
        .map(memberConverter::toModel);
  }

  @Override
  public void delete(long followId) {
    repository.deleteById(followId);
  }
}
