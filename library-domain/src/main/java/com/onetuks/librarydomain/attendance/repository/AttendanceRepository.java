package com.onetuks.librarydomain.attendance.repository;

import com.onetuks.librarydomain.attendance.model.Attendance;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository {

  Attendance create(Attendance attendance);

  int readThisMonth(long loginId);

  List<Attendance> readAllThisMonth(long loginId, LocalDate startOfMonth, LocalDate endOfMonth);
}
