package com.onetuks.librarypoint.service;

import com.onetuks.librarydomain.global.point.service.PointService;
import com.onetuks.librarypoint.repository.DailyPointLimitRepository;
import com.onetuks.librarypoint.repository.PointRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PointServiceImpl implements PointService {

  private static final long BOOK_REGISTRATION_POINT = 20L;
  private static final long REVIEW_REGISTRATION_EVENT_POINT = 30L;
  private static final long REVIEW_REGISTRATION_BASE_POINT = 15L;
  private static final long REVIEW_PICK_PICKER_POINT = 1L;
  private static final long REVIEW_PICK_RECEIVER_POINT = 5L;

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
}
