package com.onetuks.librarydomain.attendance.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.onetuks.librarydomain.AttendanceFixture;
import com.onetuks.librarydomain.DomainIntegrationTest;
import com.onetuks.librarydomain.MemberFixture;
import com.onetuks.librarydomain.attendance.model.Attendance;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.libraryobject.enums.RoleType;
import java.time.LocalDate;
import java.util.List;
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

    given(attendanceRepository.readThisMonth(member.memberId())).willReturn(0);
    given(memberRepository.read(member.memberId())).willReturn(member);
    given(attendanceRepository.create(any(Attendance.class))).willReturn(attendance);

    // When
    Attendance result = attendanceService.register(member.memberId(), today);

    // Then
    assertAll(
        () -> assertThat(result.attendanceId()).isNotNull(),
        () -> assertThat(result.member().memberId()).isEqualTo(member.memberId()),
        () -> assertThat(result.attendedAt()).isEqualTo(LocalDate.now()));

    verify(pointEventProducer, times(1)).creditPointForAttendance(member.memberId(), 1);
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

    verify(attendanceRepository, never()).readThisMonth(member.memberId());
    verify(attendanceRepository, never()).create(any(Attendance.class));
  }

  @Test
  @DisplayName("이번 달 출석 현황을 조회한다.")
  void searchAllThisMonthAttendances_Test() {
    // Given
    Member member = MemberFixture.create(103L, RoleType.USER);
    LocalDate today = LocalDate.now();
    Attendance attendance = AttendanceFixture.create(103L, member, LocalDate.now());

    given(
            attendanceRepository.readAllThisMonth(
                member.memberId(),
                today.withDayOfMonth(1),
                today.withDayOfMonth(today.lengthOfMonth())))
        .willReturn(List.of(attendance));

    // When
    List<Attendance> result =
        attendanceService.searchAllThisMonthAttendances(member.memberId(), today);

    // Then
    assertThat(result).hasSize(1);
  }
}
