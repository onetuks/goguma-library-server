package com.onetuks.librarydomain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.never;

import com.onetuks.librarydomain.DomainIntegrationTest;
import com.onetuks.librarydomain.FollowFixture;
import com.onetuks.librarydomain.MemberFixture;
import com.onetuks.librarydomain.member.model.Follow;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.libraryobject.enums.RoleType;
import com.onetuks.libraryobject.exception.ApiAccessDeniedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FollowServiceTest extends DomainIntegrationTest {

  @Test
  @DisplayName("팔로우를 등록하면 해당 팔로워 멤버의 팔로잉 카운트와 팔로이 멤버의 팔로워 카운트가 증가한다.")
  void register_Test() {
    // Given
    Member member = MemberFixture.create(101L, RoleType.USER);
    Member followee = MemberFixture.create(201L, RoleType.USER);
    Member expectedFollower = member.increaseFollowingCountStatics();
    Member expectedFollowee = followee.increaseFollowerCountStatics();

    Follow follow = FollowFixture.create(101L, expectedFollower, expectedFollowee);

    given(memberRepository.read(member.memberId())).willReturn(member);
    given(memberRepository.update(any(Member.class))).willReturn(expectedFollower);
    given(memberRepository.read(followee.memberId())).willReturn(followee);
    given(memberRepository.update(any(Member.class))).willReturn(expectedFollowee);
    given(followRepository.create(any(Follow.class))).willReturn(follow);

    // When
    Follow result = followService.register(member.memberId(), follow.followId());

    // Then
    assertAll(
        () -> assertThat(result.followId()).isNotNull(),
        () -> assertThat(result.follower().memberId()).isEqualTo(member.memberId()),
        () -> assertThat(result.followee().memberId()).isEqualTo(followee.memberId()),
        () ->
            assertThat(result.follower().memberStatics().followingCounts())
                .isEqualTo(expectedFollower.memberStatics().followingCounts()),
        () ->
            assertThat(result.followee().memberStatics().followerCounts())
                .isEqualTo(expectedFollowee.memberStatics().followerCounts()));
  }

  @Test
  @DisplayName("팔로우를 취소한다.")
  void remove_Test() {
    // Given
    Member member = MemberFixture.create(102L, RoleType.USER);
    Member followee = MemberFixture.create(202L, RoleType.USER);
    Follow follow = FollowFixture.create(102L, member, followee);

    given(memberRepository.read(member.memberId())).willReturn(member);
    given(followRepository.read(follow.followId())).willReturn(follow);

    // When
    followService.remove(member.memberId(), follow.followId());

    // Then
    verify(memberRepository, times(2)).update(any(Member.class));
    verify(followRepository, times(1)).delete(follow.followId());
  }

  @Test
  @DisplayName("권한 없는 멤버가 팔로우 취소시 예외를 던진다.")
  void remove_NotAuth_ExceptionThrown() {
    // Given
    long notAuthMemberId = 999L;
    Member member = MemberFixture.create(103L, RoleType.USER);
    Member followee = MemberFixture.create(203L, RoleType.USER);
    Follow follow = FollowFixture.create(103L, member, followee);

    given(memberRepository.read(member.memberId())).willReturn(member);
    given(followRepository.read(follow.followId())).willReturn(follow);

    // When & Then
    assertThatThrownBy(() -> followService.remove(notAuthMemberId, follow.followId()))
        .isInstanceOf(ApiAccessDeniedException.class);

    verify(memberRepository, never()).update(any(Member.class));
    verify(followRepository, never()).delete(anyLong());
  }

  @Test
  @DisplayName("팔로우 여부를 조회한다.")
  void readExistence_Test() {
    // Given
    Member follower = MemberFixture.create(104L, RoleType.USER);
    Member followee = MemberFixture.create(204L, RoleType.USER);

    given(followRepository.readExistence(follower.memberId(), followee.memberId()))
        .willReturn(true);

    // When
    boolean result = followService.searchExistence(follower.memberId(), followee.memberId());

    // Then
    assertThat(result).isTrue();
  }
}
