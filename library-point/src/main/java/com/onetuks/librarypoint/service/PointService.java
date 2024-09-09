package com.onetuks.librarypoint.service;

import static com.onetuks.librarypoint.service.model.vo.Activity.ATTENDANCE_DAILY;
import static com.onetuks.librarypoint.service.model.vo.Activity.BOOK_REGISTRATION;
import static com.onetuks.librarypoint.service.model.vo.Activity.REVIEW_PICK_PICKER;
import static com.onetuks.librarypoint.service.model.vo.Activity.REVIEW_PICK_RECEIVER;
import static com.onetuks.librarypoint.service.model.vo.Activity.REVIEW_REGISTRATION_BASE;
import static com.onetuks.librarypoint.service.model.vo.Activity.REVIEW_REGISTRATION_EVENT;

import com.onetuks.librarypoint.repository.DailyPointLimitRepository;
import com.onetuks.librarypoint.repository.PointRepository;
import com.onetuks.librarypoint.service.model.PointHistory;
import com.onetuks.librarypoint.service.model.vo.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PointService {

  private final PointRepository pointRepository;
  private final DailyPointLimitRepository dailyPointLimitRepository;

  public PointService(
      PointRepository pointRepository, DailyPointLimitRepository dailyPointLimitRepository) {
    this.pointRepository = pointRepository;
    this.dailyPointLimitRepository = dailyPointLimitRepository;
  }

  public void creditPointForBookRegistration(long memberId) {
    pointRepository.creditPoints(memberId, BOOK_REGISTRATION);
  }

  public void debitPointForBookRemoval(long memberId) {
    pointRepository.debitPoints(memberId, BOOK_REGISTRATION);
  }

  public void creditPointForReviewRegistration(long memberId) {
    pointRepository.creditPoints(memberId, REVIEW_REGISTRATION_BASE);
  }

  public void creditPointForReviewRegistrationEvent(long memberId) {
    pointRepository.creditPoints(memberId, REVIEW_REGISTRATION_EVENT);
  }

  public void debitPointForReviewRemoval(long memberId) {
    pointRepository.debitPoints(memberId, REVIEW_REGISTRATION_BASE);
  }

  public void creditPointForReviewPicker(long pickerMemberId) {
    if (dailyPointLimitRepository.isCreditable(pickerMemberId)) {
      dailyPointLimitRepository.increaseCreditCount(pickerMemberId);
      pointRepository.creditPoints(pickerMemberId, REVIEW_PICK_PICKER);
    }
  }

  public void creditPointForReviewReceiver(long receiverMemberId) {
    pointRepository.creditPointsWithLock(receiverMemberId, REVIEW_PICK_RECEIVER);
  }

  public void debitPointForReviewPicker(long pickerMemberId) {
    if (dailyPointLimitRepository.isDebitable(pickerMemberId)) {
      dailyPointLimitRepository.decreaseCreditCount(pickerMemberId);
      pointRepository.debitPoints(pickerMemberId, REVIEW_PICK_PICKER);
    }
  }

  public void creditPointForAttendance(long loginId, Activity activity) {
    if (activity != ATTENDANCE_DAILY) {
      pointRepository.creditPoints(loginId, activity);
    }
    pointRepository.creditPoints(loginId, ATTENDANCE_DAILY);
  }

  public Page<PointHistory> searchAllPointHistories(long loginId, Pageable pageable) {
    return pointRepository.readAllPointHistories(loginId, pageable);
  }
}
