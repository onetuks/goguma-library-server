package com.onetuks.librarydomain.global.point.service;

import org.springframework.stereotype.Service;

@Service
public interface PointService {

  void creditPointForBookRegistration(long memberId);

  void debitPointForBookRemoval(long memberId);

  void creditPointForReviewRegistration(long memberId, boolean isWeeklyFeaturedBook);

  void debitPointForReviewRemoval(long memberId);

  void creditPointForReviewPick(long pickerMemberId, long receiverMemberId);

  void debitPointForReviewPick(long pickerMemberId);

  void creditPointForAttendance(long loginId, int attendedCount);
}
