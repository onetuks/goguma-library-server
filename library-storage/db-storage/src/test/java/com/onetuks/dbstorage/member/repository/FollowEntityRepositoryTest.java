package com.onetuks.dbstorage.member.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.dbstorage.DbStorageIntegrationTest;
import com.onetuks.librarydomain.FollowFixture;
import com.onetuks.librarydomain.MemberFixture;
import com.onetuks.librarydomain.member.model.Follow;
import com.onetuks.libraryobject.enums.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FollowEntityRepositoryTest extends DbStorageIntegrationTest {

  @Test
  @DisplayName("팔로우십을 등록한다.")
  void create() {
    // Given
    Follow follow =
        FollowFixture.create(
            null,
            memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
            memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)));

    // When
    Follow result = followEntityRepository.create(follow);

    // Then
    assertAll(
        () -> assertThat(result.followId()).isNotNull(),
        () -> assertThat(result.follower().memberId()).isEqualTo(follow.follower().memberId()),
        () -> assertThat(result.followee().memberId()).isEqualTo(follow.followee().memberId()));
  }

  @Test
  @DisplayName("팔로우십을 조회한다.")
  void read() {
    // Given
    Follow follow =
        followEntityRepository.create(
            FollowFixture.create(
                null,
                memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
                memberEntityRepository.create(MemberFixture.create(null, RoleType.USER))));

    // When
    Follow result = followEntityRepository.read(follow.followId());

    // Then
    assertAll(
        () -> assertThat(result.followId()).isNotNull(),
        () -> assertThat(result.follower().memberId()).isEqualTo(follow.follower().memberId()),
        () -> assertThat(result.followee().memberId()).isEqualTo(follow.followee().memberId()));
  }

  @Test
  @DisplayName("팔로우십이 존재하는지 조회한다.")
  void readExistence() {
    // Given
    Follow follow =
        followEntityRepository.create(
            FollowFixture.create(
                null,
                memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
                memberEntityRepository.create(MemberFixture.create(null, RoleType.USER))));

    // When
    boolean result =
        followEntityRepository.readExistence(
            follow.follower().memberId(), follow.followee().memberId());

    // Then
    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("팔로우십을 제거한다.")
  void delete() {
    // Given
    Follow follow =
        followEntityRepository.create(
            FollowFixture.create(
                null,
                memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
                memberEntityRepository.create(MemberFixture.create(null, RoleType.USER))));

    // When
    followEntityRepository.delete(follow.followId());

    // Then
    boolean result =
        followEntityRepository.readExistence(
            follow.follower().memberId(), follow.followee().memberId());

    assertThat(result).isFalse();
  }
}
