package com.onetuks.librarydomain.member.repository;

import com.onetuks.librarydomain.member.model.Follow;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository {

  Follow create(Follow follow);

  Follow read(long followId);

  boolean readExistence(long followerId, long followeeId);

  void delete(long followId);
}
