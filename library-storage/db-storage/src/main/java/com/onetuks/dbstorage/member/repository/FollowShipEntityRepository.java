package com.onetuks.dbstorage.member.repository;

import com.onetuks.dbstorage.member.converter.FollowShipConverter;
import com.onetuks.librarydomain.member.model.FollowShip;
import com.onetuks.librarydomain.member.repository.FollowShipRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class FollowShipEntityRepository implements FollowShipRepository {

  private final FollowShipEntityJpaRepository repository;
  private final FollowShipConverter converter;

  public FollowShipEntityRepository(
      FollowShipEntityJpaRepository repository, FollowShipConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  @Override
  public FollowShip create(FollowShip followShip) {
    return converter.toModel(repository.save(converter.toEntity(followShip)));
  }

  @Override
  public FollowShip read(long followShipId) {
    return converter.toModel(
        repository
            .findById(followShipId)
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 팔로우십입니다.")));
  }

  @Override
  public boolean readExistence(long followerId, long followeeId) {
    return repository.existsByFollowerMemberIdAndFolloweeMemberId(followerId, followeeId);
  }

  @Override
  public void delete(long followShipId) {
    repository.deleteById(followShipId);
  }
}
