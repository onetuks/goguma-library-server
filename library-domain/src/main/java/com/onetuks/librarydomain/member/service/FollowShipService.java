package com.onetuks.librarydomain.member.service;

import com.onetuks.librarydomain.member.model.FollowShip;
import com.onetuks.librarydomain.member.repository.FollowShipRepository;
import com.onetuks.librarydomain.member.repository.MemberRepository;
import com.onetuks.libraryobject.exception.ApiAccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FollowShipService {

  private final FollowShipRepository followShipRepository;
  private final MemberRepository memberRepository;

  public FollowShipService(
      FollowShipRepository followShipRepository, MemberRepository memberRepository) {
    this.followShipRepository = followShipRepository;
    this.memberRepository = memberRepository;
  }

  @Transactional
  public FollowShip register(long loginId, long followeeId) {
    return followShipRepository.create(
        new FollowShip(
            null,
            memberRepository.update(memberRepository.read(loginId).increaseFollowingCountStatics()),
            memberRepository.update(
                memberRepository.read(followeeId).increaseFollowerCountStatics())));
  }

  @Transactional
  public void remove(long loginId, long followShipId) {
    FollowShip followShip = followShipRepository.read(followShipId);

    if (loginId != followShip.follower().memberId()) {
      throw new ApiAccessDeniedException("해당 팔로우십에 대한 권한이 없는 멤버입니다.");
    }

    memberRepository.update(followShip.follower().decreaseFollowingCountStatics());
    memberRepository.update(followShip.followee().decreaseFollowerCountStatics());

    followShipRepository.delete(followShipId);
  }

  @Transactional(readOnly = true)
  public boolean searchExistence(long loginId, long followeeId) {
    return followShipRepository.readExistence(loginId, followeeId);
  }
}
