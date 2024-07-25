package com.onetuks.librarydomain;

import com.onetuks.librarydomain.attendance.model.Attendance;
import com.onetuks.librarydomain.member.model.Member;
import java.time.LocalDate;

public class AttendanceFixture {

  public static Attendance create(Long attendanceId, Member member, LocalDate attendedAt) {
    return new Attendance(attendanceId, member, attendedAt);
  }
}
