package com.onetuks.librarydomain.member.service;

import com.onetuks.librarydomain.member.model.FollowShip;
import com.onetuks.librarydomain.member.repository.FollowShipRepository;
import com.onetuks.librarydomain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FollowShipService {

  private final FollowShipRepository followShipRepository;
  private final MemberRepository memberRepository;

  public FollowShipService(
      FollowShipRepository followShipRepository,
      MemberRepository memberRepository) {
    this.followShipRepository = followShipRepository;
    this.memberRepository = memberRepository;
  }

  @Transactional
  public FollowShip register(long loginId, long followeeId) {
    return followShipRepository.create(
        new FollowShip(null, memberRepository.read(loginId), memberRepository.read(followeeId)));
  }
}
