package com.onetuks.librarydomain;

import com.onetuks.librarydomain.attendance.model.Attendance;
import com.onetuks.librarydomain.member.model.Member;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class AttendanceFixture {

  private static final int ATTENDANCE_DATE_FORMAT_INFO_LENGTH = 3;

  public static Attendance create(Long attendanceId, Member member, String attendanceDate) {
    return new Attendance(attendanceId, member, convertAttendanceFormatToDate(attendanceDate));
  }

  private static LocalDate convertAttendanceFormatToDate(String attendanceDate) {
    if (attendanceDate == null) {
      return LocalDate.now();
    }

    attendanceDate = attendanceDate.replaceAll("-+", "-");

    List<Integer> dateInfo =
        Arrays.stream(attendanceDate.split("-")).map(Integer::valueOf).toList();

    if (dateInfo.size() != ATTENDANCE_DATE_FORMAT_INFO_LENGTH) {
      throw new IllegalArgumentException("Invalid date format: " + attendanceDate);
    } else if (dateInfo.get(1) < 12 || dateInfo.getLast() > 31) {
      throw new IllegalArgumentException("Invalid date value: " + attendanceDate);
    }

    return LocalDate.of(dateInfo.getFirst(), dateInfo.get(1), dateInfo.getLast());
  }
}
