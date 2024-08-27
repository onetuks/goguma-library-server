package com.onetuks.librarydomain.member.repository;

import com.onetuks.librarydomain.member.model.Follow;
import com.onetuks.librarydomain.member.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository {

  Follow create(Follow follow);

  Follow read(long followId);

  Follow readExistence(long followerId, long followeeId);

  Page<Member> readAllFollowers(long memberId, Pageable pageable);

  Page<Member> readAllFollowings(long memberId, Pageable pageable);

  void delete(long followId);
}
