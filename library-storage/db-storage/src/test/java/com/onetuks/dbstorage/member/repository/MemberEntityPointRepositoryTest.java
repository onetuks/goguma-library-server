package com.onetuks.dbstorage.member.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

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
    Member member = memberEntityRepository.create(MemberFixture.create(null, RoleType.USER));
    long augmentedPoints = 20L;

    // When
    pointRepository.creditPoints(member.memberId(), augmentedPoints);

    // Then
    long result = memberEntityRepository.read(member.memberId()).points();

    assertThat(result).isEqualTo(member.points() + augmentedPoints);
  }
}
