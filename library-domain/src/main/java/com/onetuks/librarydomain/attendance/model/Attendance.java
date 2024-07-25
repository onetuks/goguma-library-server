package com.onetuks.librarydomain.attendance.model;

import com.onetuks.librarydomain.member.model.Member;
import java.time.LocalDate;
import java.util.Objects;

public record Attendance(Long attendanceId, Member member, LocalDate attendedAt) {

  public Attendance {
    attendedAt = Objects.requireNonNullElse(attendedAt, LocalDate.now());
  }
}
