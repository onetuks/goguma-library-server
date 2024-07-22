package com.onetuks.librarydomain.member.repository;

import com.onetuks.librarydomain.member.model.FollowShip;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowShipRepository {

  FollowShip create(FollowShip followShip);

  FollowShip read(long followShipId);

  boolean readExistence(long followerId, long followeeId);

  void delete(long followShipId);
}
