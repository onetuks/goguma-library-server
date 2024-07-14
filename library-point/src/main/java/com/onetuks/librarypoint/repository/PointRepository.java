package com.onetuks.librarypoint.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository {

  void creditPoints(long memberId, long creditPoint);

  void debitPoints(long memberId, long debitPoint);
}
