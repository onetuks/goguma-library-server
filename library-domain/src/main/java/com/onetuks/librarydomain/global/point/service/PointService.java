package com.onetuks.librarydomain.global.point.service;

import org.springframework.stereotype.Service;

@Service
public interface PointService {

  long BOOK_REGISTRATION_POINT = 20L;
  long REVIEW_REGISTRATION_EVENT_POINT = 30L;
  long REVIEW_REGISTRATION_BASE_POINT = 15L;
  long REVIEW_PICK_PICKER_POINT = 1L;
  long REVIEW_PICK_RECEIVER_POINT = 5L;
  long ATTENDANCE_DAILY_POINT = 1L;
  long ATTENDANCE_3_DAYS_POINT = 3L;
  long ATTENDANCE_5_DAYS_POINT = 5L;
  long ATTENDANCE_10_DAYS_POINT = 10L;
  long ATTENDANCE_30_DAYS_POINT = 30L;

  void creditPointForBookRegistration(long memberId);

  void creditPointForReviewRegistration(long memberId, boolean isWeeklyFeaturedBook);

  void debitPointForReviewRemoval(long memberId);

  void creditPointForReviewPick(long pickerMemberId, long receiverMemberId);

  void debitPointForReviewPick(long pickerMemberId);

  void creditPointForAttendance(long loginId, int attendedCount);
}
