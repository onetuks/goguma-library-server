package com.onetuks.librarydomain.attendance.service;

import com.onetuks.librarydomain.attendance.model.Attendance;
import com.onetuks.librarydomain.attendance.repository.AttendanceRepository;
import com.onetuks.librarydomain.member.repository.MemberRepository;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AttendanceService {

  private final AttendanceRepository attendanceRepository;
  private final MemberRepository memberRepository;

  public AttendanceService(
      AttendanceRepository attendanceRepository,
      MemberRepository memberRepository) {
    this.attendanceRepository = attendanceRepository;
    this.memberRepository = memberRepository;
  }

  @Transactional
  public Attendance register(long loginId, LocalDate date) {
    if (!date.isEqual(LocalDate.now())) {
      throw new IllegalArgumentException("오늘 날짜만 출석 체크가 가능합니다.");
    }

    return attendanceRepository.create(new Attendance(null, memberRepository.read(loginId), null));
  }
}
