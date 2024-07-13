package com.onetuks.dbstorage.member.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.onetuks.dbstorage.DbStorageIntegrationTest;
import com.onetuks.librarydomain.MemberFixture;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.libraryobject.enums.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberEntityPointRepositoryTest extends DbStorageIntegrationTest {

  @Test
  @DisplayName("포인트를 증가시킨다.")
  void creditPoints() {
    // Given
    long augmentedPoints = 20L;
    Member member = memberEntityRepository.create(MemberFixture.create(null, RoleType.USER));

    // When
    pointRepository.creditPoints(member.memberId(), augmentedPoints);

    // Then
    long result = memberEntityRepository.read(member.memberId()).points();

    assertThat(result).isEqualTo(member.points() + augmentedPoints);
  }

  @Test
  @DisplayName("포인트를 감소시킨다.")
  void debitPoints() {
    // Given
    long augmentedPoints = 20L;
    long diminishedPoints = 10L;
    Member member = memberEntityRepository.create(MemberFixture.create(null, RoleType.USER));

    pointRepository.creditPoints(member.memberId(), augmentedPoints);

    // When
    pointRepository.debitPoints(member.memberId(), diminishedPoints);

    // Then
    long result = memberEntityRepository.read(member.memberId()).points();

    assertThat(result).isEqualTo(member.points() + augmentedPoints - diminishedPoints);
  }

  @Test
  @DisplayName("포인트 변동값을 음수로 설정한 경우 예외를 던진다.")
  void minusPoint_Exception() {
    // Given
    long invalidPointValue = -1 * 20L;
    Member member = memberEntityRepository.create(MemberFixture.create(null, RoleType.USER));

    // When & Then
    assertThatThrownBy(() -> pointRepository.creditPoints(member.memberId(), invalidPointValue))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
