package com.onetuks.librarydomain.member.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository {

  long BOOK_REGISTRATION_POINT = 20L;

  void creditPoints(long memberId, long point);
}
