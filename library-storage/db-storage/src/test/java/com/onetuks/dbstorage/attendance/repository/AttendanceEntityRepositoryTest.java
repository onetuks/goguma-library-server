package com.onetuks.dbstorage.attendance.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.dbstorage.DbStorageIntegrationTest;
import com.onetuks.librarydomain.AttendanceFixture;
import com.onetuks.librarydomain.MemberFixture;
import com.onetuks.librarydomain.attendance.model.Attendance;
import com.onetuks.libraryobject.enums.RoleType;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AttendanceEntityRepositoryTest extends DbStorageIntegrationTest {

  @Test
  @DisplayName("출석 정보를 저장한다.")
  void create() {
    // Given
    Attendance attendance =
        AttendanceFixture.create(
            101L, memberEntityRepository.create(MemberFixture.create(101L, RoleType.USER)), null);

    // When
    Attendance result = attendanceEntityRepository.create(attendance);

    // Then
    assertAll(
        () -> assertThat(result.attendanceId()).isNotNull(),
        () -> assertThat(result.member().memberId()).isEqualTo(attendance.member().memberId()),
        () -> assertThat(result.attendedAt()).isEqualTo(LocalDate.now()));
  }
}
