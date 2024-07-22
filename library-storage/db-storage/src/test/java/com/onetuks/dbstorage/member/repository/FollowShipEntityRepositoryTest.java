package com.onetuks.dbstorage.member.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.dbstorage.DbStorageIntegrationTest;
import com.onetuks.librarydomain.FollowShipFixture;
import com.onetuks.librarydomain.MemberFixture;
import com.onetuks.librarydomain.member.model.FollowShip;
import com.onetuks.libraryobject.enums.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FollowShipEntityRepositoryTest extends DbStorageIntegrationTest {

  @Test
  @DisplayName("팔로우십을 등록한다.")
  void create() {
    // Given
    FollowShip followShip =
        FollowShipFixture.create(
            null,
            memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
            memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)));

    // When
    FollowShip result = followShipEntityRepository.create(followShip);

    // Then
    assertAll(
        () -> assertThat(result.followShipId()).isNotNull(),
        () -> assertThat(result.follower().memberId()).isEqualTo(followShip.follower().memberId()),
        () -> assertThat(result.followee().memberId()).isEqualTo(followShip.followee().memberId()));
  }

  @Test
  @DisplayName("팔로우십을 조회한다.")
  void read() {
    // Given
    FollowShip followShip =
        followShipEntityRepository.create(
            FollowShipFixture.create(
                null,
                memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
                memberEntityRepository.create(MemberFixture.create(null, RoleType.USER))));

    // When
    FollowShip result = followShipEntityRepository.read(followShip.followShipId());

    // Then
    assertAll(
        () -> assertThat(result.followShipId()).isNotNull(),
        () -> assertThat(result.follower().memberId()).isEqualTo(followShip.follower().memberId()),
        () -> assertThat(result.followee().memberId()).isEqualTo(followShip.followee().memberId()));
  }

  @Test
  @DisplayName("팔로우십이 존재하는지 조회한다.")
  void readExistence() {
    // Given
    FollowShip followShip =
        followShipEntityRepository.create(
            FollowShipFixture.create(
                null,
                memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
                memberEntityRepository.create(MemberFixture.create(null, RoleType.USER))));

    // When
    boolean result =
        followShipEntityRepository.readExistence(
            followShip.follower().memberId(), followShip.followee().memberId());

    // Then
    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("팔로우십을 제거한다.")
  void delete() {
    // Given
    FollowShip followShip =
        followShipEntityRepository.create(
            FollowShipFixture.create(
                null,
                memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
                memberEntityRepository.create(MemberFixture.create(null, RoleType.USER))));

    // When
    followShipEntityRepository.delete(followShip.followShipId());

    // Then
    boolean result =
        followShipEntityRepository.readExistence(
            followShip.follower().memberId(), followShip.followee().memberId());

    assertThat(result).isFalse();
  }
}
