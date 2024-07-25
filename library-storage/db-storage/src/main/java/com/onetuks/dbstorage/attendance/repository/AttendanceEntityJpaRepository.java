package com.onetuks.dbstorage.attendance.repository;

import com.onetuks.dbstorage.attendance.entity.AttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceEntityJpaRepository extends JpaRepository<AttendanceEntity, Long> {}
