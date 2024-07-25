package com.onetuks.dbstorage.attendance.converter;

import com.onetuks.dbstorage.attendance.entity.AttendanceEntity;
import com.onetuks.dbstorage.member.converter.MemberConverter;
import com.onetuks.librarydomain.attendance.model.Attendance;
import org.springframework.stereotype.Component;

@Component
public class AttendanceConverter {

  private final MemberConverter memberConverter;

  public AttendanceConverter(MemberConverter memberConverter) {
    this.memberConverter = memberConverter;
  }

  public AttendanceEntity toEntity(Attendance model) {
    return new AttendanceEntity(
        model.attendanceId(), memberConverter.toEntity(model.member()), model.attendedAt());
  }

  public Attendance toModel(AttendanceEntity entity) {
    return new Attendance(
        entity.getAttendanceId(),
        memberConverter.toModel(entity.getMemberEntity()),
        entity.getAttendedAt());
  }
}
