package com.onetuks.librarydomain.attendance.service;

import com.onetuks.librarydomain.attendance.repository.AttendanceRepository;
import org.springframework.stereotype.Service;

@Service
public class AttendanceService {

  private final AttendanceRepository attendanceRepository;

  public AttendanceService(AttendanceRepository attendanceRepository) {
    this.attendanceRepository = attendanceRepository;
  }
}
