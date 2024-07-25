package com.onetuks.librarypoint.service;

import com.onetuks.librarydomain.global.point.service.PointService;
import com.onetuks.librarypoint.repository.DailyPointLimitRepository;
import com.onetuks.librarypoint.repository.PointRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PointServiceImpl implements PointService {

  private final PointRepository pointRepository;
  private final DailyPointLimitRepository dailyPointLimitRepository;

  public PointServiceImpl(
      PointRepository pointRepository, DailyPointLimitRepository dailyPointLimitRepository) {
    this.pointRepository = pointRepository;
    this.dailyPointLimitRepository = dailyPointLimitRepository;
  }

  @Override
  @Transactional
  public void creditPointForBookRegistration(long memberId) {
    pointRepository.creditPoints(memberId, BOOK_REGISTRATION_POINT);
  }

  @Override
  @Transactional
  public void creditPointForReviewRegistration(long memberId, boolean isWeeklyFeaturedBook) {
    pointRepository.creditPoints(
        memberId,
        isWeeklyFeaturedBook ? REVIEW_REGISTRATION_EVENT_POINT : REVIEW_REGISTRATION_BASE_POINT);
  }

  @Override
  @Transactional
  public void debitPointForReviewRemoval(long memberId) {
    pointRepository.debitPoints(memberId, REVIEW_REGISTRATION_BASE_POINT);
  }

  @Override
  @Transactional
  public void creditPointForReviewPick(long pickerMemberId, long receiverMemberId) {
    if (dailyPointLimitRepository.isCreditable(pickerMemberId)) {
      dailyPointLimitRepository.increaseCreditCount(pickerMemberId);
      pointRepository.creditPoints(pickerMemberId, REVIEW_PICK_PICKER_POINT);
      pointRepository.creditPoints(receiverMemberId, REVIEW_PICK_RECEIVER_POINT);
    }
  }

  @Override
  @Transactional
  public void debitPointForReviewPick(long pickerMemberId) {
    if (dailyPointLimitRepository.isDebitable(pickerMemberId)) {
      dailyPointLimitRepository.decreaseCreditCount(pickerMemberId);
      pointRepository.debitPoints(pickerMemberId, REVIEW_PICK_PICKER_POINT);
    }
  }

  @Override
  public void creditPointForAttendance(long loginId, int attendedCount) {
    switch (attendedCount) {
      case 3 -> pointRepository.creditPoints(loginId, ATTENDANCE_3_DAYS_POINT);
      case 5 -> pointRepository.creditPoints(loginId, ATTENDANCE_5_DAYS_POINT);
      case 10 -> pointRepository.creditPoints(loginId, ATTENDANCE_10_DAYS_POINT);
      case 30 -> pointRepository.creditPoints(loginId, ATTENDANCE_30_DAYS_POINT);
    }

    pointRepository.creditPoints(loginId, ATTENDANCE_DAILY_POINT);
  }
}
