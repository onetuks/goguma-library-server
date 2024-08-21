package com.onetuks.libraryapi.attendance.dto.response;

import com.onetuks.librarydomain.attendance.model.Attendance;
import java.time.LocalDate;
import java.util.List;

public record AttendanceResponse(long attendanceId, long memberId, LocalDate attendedAt) {

  public static AttendanceResponse from(Attendance model) {
    return new AttendanceResponse(
        model.attendanceId(), model.member().memberId(), model.attendedAt());
  }

  public record AttendanceResponses(List<AttendanceResponse> responses) {

    public static AttendanceResponses from(List<Attendance> results) {
      return new AttendanceResponses(results.stream().map(AttendanceResponse::from).toList());
    }
  }
}
