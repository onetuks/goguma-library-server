package com.onetuks.librarydomain.member.service;

import com.onetuks.librarydomain.member.model.Follow;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.member.repository.FollowRepository;
import com.onetuks.librarydomain.member.repository.MemberRepository;
import com.onetuks.libraryobject.exception.ApiAccessDeniedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FollowService {

  private final FollowRepository followRepository;
  private final MemberRepository memberRepository;

  public FollowService(FollowRepository followRepository, MemberRepository memberRepository) {
    this.followRepository = followRepository;
    this.memberRepository = memberRepository;
  }

  @Transactional
  public Follow register(long loginId, long followeeId) {
    return followRepository.create(
        new Follow(
            null,
            memberRepository.update(memberRepository.read(loginId).increaseFollowingCountStatics()),
            memberRepository.update(
                memberRepository.read(followeeId).increaseFollowerCountStatics())));
  }

  @Transactional
  public void remove(long loginId, long followId) {
    Follow follow = followRepository.read(followId);

    if (loginId != follow.follower().memberId()) {
      throw new ApiAccessDeniedException("해당 팔로우십에 대한 권한이 없는 멤버입니다.");
    }

    memberRepository.update(follow.follower().decreaseFollowingCountStatics());
    memberRepository.update(follow.followee().decreaseFollowerCountStatics());

    followRepository.delete(followId);
  }

  @Transactional(readOnly = true)
  public boolean searchExistence(long loginId, long followeeId) {
    return followRepository.readExistence(loginId, followeeId);
  }

  @Transactional(readOnly = true)
  public Page<Member> searchAllFollowers(long memberId, Pageable pageable) {
    return followRepository.readAllFollowers(memberId, pageable);
  }

  @Transactional(readOnly = true)
  public Page<Member> searchAllFollowings(long memberId, Pageable pageable) {
    return followRepository.readAllFollowings(memberId, pageable);
  }
}
