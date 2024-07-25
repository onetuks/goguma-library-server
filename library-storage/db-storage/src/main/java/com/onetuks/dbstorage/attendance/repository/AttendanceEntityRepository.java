package com.onetuks.dbstorage.attendance.repository;

import com.onetuks.dbstorage.attendance.converter.AttendanceConverter;
import com.onetuks.librarydomain.attendance.model.Attendance;
import com.onetuks.librarydomain.attendance.repository.AttendanceRepository;
import java.time.LocalDate;
import org.springframework.stereotype.Repository;

@Repository
public class AttendanceEntityRepository implements AttendanceRepository {

  private final AttendanceEntityJpaRepository repository;
  private final AttendanceConverter converter;

  public AttendanceEntityRepository(
      AttendanceEntityJpaRepository repository, AttendanceConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  @Override
  public Attendance create(Attendance attendance) {
    return converter.toModel(repository.save(converter.toEntity(attendance)));
  }

  @Override
  public int readThisMonth(long loginId) {
    return repository.countByMemberEntityMemberIdAndAttendedAtGreaterThanEqual(
        loginId, LocalDate.now().withDayOfMonth(1));
  }
}
