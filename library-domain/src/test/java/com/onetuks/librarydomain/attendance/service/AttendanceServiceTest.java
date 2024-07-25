package com.onetuks.librarydomain.attendance.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.onetuks.librarydomain.AttendanceFixture;
import com.onetuks.librarydomain.DomainIntegrationTest;
import com.onetuks.librarydomain.MemberFixture;
import com.onetuks.librarydomain.attendance.model.Attendance;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.libraryobject.enums.RoleType;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AttendanceServiceTest extends DomainIntegrationTest {

  @Test
  @DisplayName("오늘 날짜에 출석하는 경우 성공적으로 출석된다.")
  void register_Today_Test() {
    // Given
    Member member = MemberFixture.create(101L, RoleType.USER);
    LocalDate today = LocalDate.now();
    Attendance attendance = AttendanceFixture.create(101L, member, LocalDate.now());

    given(memberRepository.read(member.memberId())).willReturn(member);
    given(attendanceRepository.create(any(Attendance.class))).willReturn(attendance);

    // When
    Attendance result = attendanceService.register(member.memberId(), today);

    // Then
    assertAll(
        () -> assertThat(result.attendanceId()).isNotNull(),
        () -> assertThat(result.member().memberId()).isEqualTo(member.memberId()),
        () -> assertThat(result.attendedAt()).isEqualTo(LocalDate.now()));
  }

  @Test
  @DisplayName("오늘 날짜가 아닌 날을 출석하는 경우 예외를 던진다.")
  void register_NotToday_ExceptionThrown() {
    // Given
    Member member = MemberFixture.create(102L, RoleType.USER);
    LocalDate notToday = LocalDate.of(2021, 1, 1);

    // When & Then
    assertThatThrownBy(() -> attendanceService.register(member.memberId(), notToday))
        .isInstanceOf(IllegalArgumentException.class);
  }
}