package com.onetuks.dbstorage.attendance.repository;

import com.onetuks.dbstorage.attendance.entity.AttendanceEntity;
import com.onetuks.dbstorage.member.entity.MemberEntity;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceEntityJpaRepository extends JpaRepository<AttendanceEntity, Long> {

  int countByMemberEntityMemberIdAndAttendedAtGreaterThanEqual(
      long loginId, LocalDate firstDayOfMonth);

  List<AttendanceEntity> findAllByMemberEntityMemberIdAndAttendedAtBetween(
      long loginId, LocalDate startOfMonth, LocalDate endOfMonth);

  void deleteAllByMemberEntity(MemberEntity memberEntity);
}
