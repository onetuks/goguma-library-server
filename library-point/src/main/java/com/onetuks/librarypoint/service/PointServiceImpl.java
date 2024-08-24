package com.onetuks.librarypoint.service;

import static com.onetuks.librarypoint.service.model.vo.Activity.ACTIVITY_10_DAYS;
import static com.onetuks.librarypoint.service.model.vo.Activity.ACTIVITY_30_DAYS;
import static com.onetuks.librarypoint.service.model.vo.Activity.ATTENDANCE_3_DAYS;
import static com.onetuks.librarypoint.service.model.vo.Activity.ATTENDANCE_5_DAYS;
import static com.onetuks.librarypoint.service.model.vo.Activity.ATTENDANCE_DAILY;
import static com.onetuks.librarypoint.service.model.vo.Activity.BOOK_REGISTRATION;
import static com.onetuks.librarypoint.service.model.vo.Activity.REVIEW_PICK_PICKER;
import static com.onetuks.librarypoint.service.model.vo.Activity.REVIEW_PICK_RECEIVER;
import static com.onetuks.librarypoint.service.model.vo.Activity.REVIEW_REGISTRATION_BASE;
import static com.onetuks.librarypoint.service.model.vo.Activity.REVIEW_REGISTRATION_EVENT;

import com.onetuks.librarydomain.global.point.service.PointService;
import com.onetuks.librarypoint.repository.DailyPointLimitRepository;
import com.onetuks.librarypoint.repository.PointRepository;
import com.onetuks.librarypoint.service.model.PointHistory;
import com.onetuks.librarypoint.service.model.vo.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PointServiceImpl implements PointService {

  private final PointRepository pointRepository;
  private final DailyPointLimitRepository dailyPointLimitRepository;

  public PointServiceImpl(
      PointRepository pointRepository,
      DailyPointLimitRepository dailyPointLimitRepository) {
    this.pointRepository = pointRepository;
    this.dailyPointLimitRepository = dailyPointLimitRepository;
  }

  @Override
  @Transactional
  public void creditPointForBookRegistration(long memberId) {
    pointRepository.creditPoints(memberId, BOOK_REGISTRATION);
  }

  @Override
  @Transactional
  public void debitPointForBookRemoval(long memberId) {
    pointRepository.debitPoints(memberId, BOOK_REGISTRATION);
  }

  @Override
  @Transactional
  public void creditPointForReviewRegistration(long memberId, boolean isWeeklyFeaturedBook) {
    pointRepository.creditPoints(
        memberId, isWeeklyFeaturedBook ? REVIEW_REGISTRATION_EVENT : REVIEW_REGISTRATION_BASE);
  }

  @Override
  @Transactional
  public void debitPointForReviewRemoval(long memberId) {
    pointRepository.debitPoints(memberId, REVIEW_REGISTRATION_BASE);
  }

  @Override
  @Transactional
  public void creditPointForReviewPick(long pickerMemberId, long receiverMemberId) {
    if (dailyPointLimitRepository.isCreditable(pickerMemberId)) {
      dailyPointLimitRepository.increaseCreditCount(pickerMemberId);
      pointRepository.creditPoints(pickerMemberId, REVIEW_PICK_PICKER);
      pointRepository.creditPointsWithLock(receiverMemberId, REVIEW_PICK_RECEIVER);
    }
  }

  @Override
  @Transactional
  public void debitPointForReviewPick(long pickerMemberId) {
    if (dailyPointLimitRepository.isDebitable(pickerMemberId)) {
      dailyPointLimitRepository.decreaseCreditCount(pickerMemberId);
      pointRepository.debitPoints(pickerMemberId, REVIEW_PICK_PICKER);
    }
  }

  @Override
  @Transactional
  public void creditPointForAttendance(long loginId, int attendedCount) {
    switch (attendedCount) {
      case 3 -> pointRepository.creditPoints(loginId, ATTENDANCE_3_DAYS);
      case 5 -> pointRepository.creditPoints(loginId, ATTENDANCE_5_DAYS);
      case 10 -> pointRepository.creditPoints(loginId, ACTIVITY_10_DAYS);
      case 30 -> pointRepository.creditPoints(loginId, ACTIVITY_30_DAYS);
    }

    pointRepository.creditPoints(loginId, ATTENDANCE_DAILY);
  }

  @Transactional
  public Page<PointHistory> searchAllPointHistories(long loginId, Pageable pageable) {
    return pointRepository.readAllPointHistories(loginId, pageable);
  }
}
