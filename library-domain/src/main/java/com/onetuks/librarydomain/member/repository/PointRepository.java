package com.onetuks.librarydomain.member.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository {

  long BOOK_REGISTRATION_POINT = 20L;
  long REVIEW_THIS_WEEK_BOOK_REGISTRATION_POINT = 30L;
  long REVIEW_BASE_REGISTRATION_POINT = 15L;

  void creditPoints(long memberId, long point);

  void debitPoints(long memberId, long point);
}
