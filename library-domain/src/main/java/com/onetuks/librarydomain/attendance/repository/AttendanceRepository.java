package com.onetuks.librarydomain.attendance.repository;

import com.onetuks.librarydomain.attendance.model.Attendance;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository {

  Attendance create(Attendance attendance);

  int readThisMonth(long loginId);
}
