package com.onetuks.dbstorage.attendance.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.dbstorage.DbStorageIntegrationTest;
import com.onetuks.librarydomain.AttendanceFixture;
import com.onetuks.librarydomain.MemberFixture;
import com.onetuks.librarydomain.attendance.model.Attendance;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.libraryobject.enums.RoleType;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

class AttendanceEntityRepositoryTest extends DbStorageIntegrationTest {

  @Test
  @DisplayName("출석 정보를 저장한다.")
  void create() {
    // Given
    Attendance attendance =
        AttendanceFixture.create(
            null,
            memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
            LocalDate.now());

    // When
    Attendance result = attendanceEntityRepository.create(attendance);

    // Then
    assertAll(
        () -> assertThat(result.attendanceId()).isNotNull(),
        () -> assertThat(result.member().memberId()).isEqualTo(attendance.member().memberId()),
        () -> assertThat(result.attendedAt()).isEqualTo(LocalDate.now()));
  }

  @Test
  @DisplayName("중복된 출석 체크 저장 시 예외를 던진다.")
  void create_DuplicateRecord_ExceptionThrown() {
    // Given
    Attendance attendance =
        attendanceEntityRepository.create(
            AttendanceFixture.create(
                null,
                memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
                LocalDate.now()));
    Attendance newAttendance =
        AttendanceFixture.create(null, attendance.member(), attendance.attendedAt());

    // When & Then
    assertThatThrownBy(() -> attendanceEntityRepository.create(newAttendance))
        .isInstanceOf(DataIntegrityViolationException.class);
  }

  @Test
  @DisplayName("이번 달 출석 횟수를 조회한다.")
  void readThisMonth_Test() {
    // Given
    Member member = memberEntityRepository.create(MemberFixture.create(null, RoleType.USER));
    attendanceEntityRepository.create(AttendanceFixture.create(null, member, LocalDate.now()));

    // When
    int result = attendanceEntityRepository.readThisMonth(member.memberId());

    // Then
    assertThat(result).isEqualTo(1);
  }

  @Test
  @DisplayName("이번 달 출석 현황을 조회한다.")
  void readAllThisMonth_Test() {
    // Given
    Member member = memberEntityRepository.create(MemberFixture.create(null, RoleType.USER));
    attendanceEntityRepository.create(AttendanceFixture.create(null, member, LocalDate.now()));
    LocalDate today = LocalDate.now();
    YearMonth yearMonth = YearMonth.from(today);
    LocalDate startOfMonth = yearMonth.atDay(1);
    LocalDate endOfMonth = yearMonth.atEndOfMonth();

    // When
    List<Attendance> results =
        attendanceEntityRepository.readAllThisMonth(member.memberId(), startOfMonth, endOfMonth);

    // Then
    assertThat(results).hasSize(1);
  }
}
