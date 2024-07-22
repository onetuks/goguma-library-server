package com.onetuks.dbstorage.member.repository;

import com.onetuks.dbstorage.member.converter.FollowConverter;
import com.onetuks.librarydomain.member.model.Follow;
import com.onetuks.librarydomain.member.repository.FollowRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class FollowEntityRepository implements FollowRepository {

  private final FollowEntityJpaRepository repository;
  private final FollowConverter converter;

  public FollowEntityRepository(
      FollowEntityJpaRepository repository, FollowConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  @Override
  public Follow create(Follow follow) {
    return converter.toModel(repository.save(converter.toEntity(follow)));
  }

  @Override
  public Follow read(long followId) {
    return converter.toModel(
        repository
            .findById(followId)
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 팔로우십입니다.")));
  }

  @Override
  public boolean readExistence(long followerId, long followeeId) {
    return repository.existsByFollowerMemberIdAndFolloweeMemberId(followerId, followeeId);
  }

  @Override
  public void delete(long followId) {
    repository.deleteById(followId);
  }
}
