package com.onetuks.librarydomain.member.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.onetuks.librarydomain.DomainIntegrationTest;
import com.onetuks.librarydomain.FollowShipFixture;
import com.onetuks.librarydomain.MemberFixture;
import com.onetuks.librarydomain.member.model.FollowShip;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.libraryobject.enums.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FollowShipServiceTest extends DomainIntegrationTest {

  @Test
  @DisplayName("팔로우를 등록한다.")
  void register() {
    // Given
    Member member = MemberFixture.create(101L, RoleType.USER);
    Member followee = MemberFixture.create(102L, RoleType.USER);
    FollowShip followShip = FollowShipFixture.create(101L, member, followee);

    given(memberRepository.read(member.memberId())).willReturn(member);
    given(memberRepository.read(followee.memberId())).willReturn(followee);
    given(followShipRepository.create(any(FollowShip.class))).willReturn(followShip);

    // When
    FollowShip result = followShipService.register(member.memberId(), followShip.followShipId());

    // Then
    assertAll(
        () -> assertThat(result.followShipId()).isNotNull(),
        () -> assertThat(result.follower().memberId()).isEqualTo(member.memberId()),
        () -> assertThat(result.followee().memberId()).isEqualTo(followee.memberId()));
  }
}