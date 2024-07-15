package com.onetuks.librarydomain.global.point.service;

import org.springframework.stereotype.Service;

@Service
public interface PointService {

  void creditPointForBookRegistration(long memberId);

  void creditPointForReviewRegistration(long memberId);

  void debitPointForReviewRemoval(long memberId);

  void creditPointForReviewPick(long pickerMemberId, long receiverMemberId);

  void debitPointForReviewPick(long pickerMemberId);
}
