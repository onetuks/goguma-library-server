package com.onetuks.dbstorage.member.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.dbstorage.DbStorageIntegrationTest;
import com.onetuks.librarydomain.FollowFixture;
import com.onetuks.librarydomain.MemberFixture;
import com.onetuks.librarydomain.member.model.Follow;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.libraryobject.enums.RoleType;
import com.onetuks.libraryobject.exception.NoSuchEntityException;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
    Follow result =
        followEntityRepository.readExistence(
            follow.follower().memberId(), follow.followee().memberId());

    // Then
    assertAll(
        () -> assertThat(result.followId()).isNotNull(),
        () -> assertThat(result.follower().memberId()).isEqualTo(follow.follower().memberId()),
        () -> assertThat(result.followee().memberId()).isEqualTo(follow.followee().memberId()));
  }

  @Test
  @DisplayName("팔로워를 조회한다.")
  void readAllFollowers_Test() {
    // Given
    int count = 5;
    Pageable pageable = PageRequest.of(0, 10);
    Member followee = memberEntityRepository.create(MemberFixture.create(null, RoleType.USER));
    IntStream.range(0, count)
        .forEach(
            i ->
                followEntityRepository.create(
                    FollowFixture.create(
                        null,
                        memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
                        followee)));

    // When
    Page<Member> results = followEntityRepository.readAllFollowers(followee.memberId(), pageable);

    // Then
    assertThat(results).hasSize(Math.min(count, pageable.getPageSize()));
  }

  @Test
  @DisplayName("팔로잉을 조회한다.")
  void readAllFollowings_Test() {
    // Given
    int count = 5;
    Pageable pageable = PageRequest.of(0, 10);
    Member follower = memberEntityRepository.create(MemberFixture.create(null, RoleType.USER));
    IntStream.range(0, count)
        .forEach(
            i ->
                followEntityRepository.create(
                    FollowFixture.create(
                        null,
                        follower,
                        memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)))));

    // When
    Page<Member> results = followEntityRepository.readAllFollowings(follower.memberId(), pageable);

    // Then
    assertThat(results).hasSize(Math.min(count, pageable.getPageSize()));
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
    assertThatThrownBy(
            () ->
                followEntityRepository.readExistence(
                    follow.follower().memberId(), follow.followee().memberId()))
        .isInstanceOf(NoSuchEntityException.class);
  }
}
