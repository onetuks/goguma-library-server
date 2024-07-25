package com.onetuks.libraryapi.attendance.dto.response;

import com.onetuks.librarydomain.attendance.model.Attendance;
import java.time.LocalDate;

public record AttendanceResponse(
    long attendanceId,
    long memberId,
    LocalDate attendedAt) {

  public static AttendanceResponse from(Attendance model) {
    return new AttendanceResponse(
        model.attendanceId(),
        model.member().memberId(),
        model.attendedAt());
  }
}
