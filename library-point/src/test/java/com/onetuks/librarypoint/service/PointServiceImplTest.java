package com.onetuks.librarypoint.service;

import static com.onetuks.librarydomain.global.point.service.PointService.ATTENDANCE_10_DAYS_POINT;
import static com.onetuks.librarydomain.global.point.service.PointService.ATTENDANCE_30_DAYS_POINT;
import static com.onetuks.librarydomain.global.point.service.PointService.ATTENDANCE_3_DAYS_POINT;
import static com.onetuks.librarydomain.global.point.service.PointService.ATTENDANCE_5_DAYS_POINT;
import static com.onetuks.librarydomain.global.point.service.PointService.ATTENDANCE_DAILY_POINT;
import static org.assertj.core.api.Assertions.assertThat;

import com.onetuks.dbstorage.member.entity.MemberEntity;
import com.onetuks.librarydomain.global.point.service.PointService;
import com.onetuks.librarypoint.CorePointIntegrationTest;
import com.onetuks.librarypoint.fixture.MemberEntityFixture;
import com.onetuks.librarypoint.repository.entity.DailyPointLimit;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PointServiceImplTest extends CorePointIntegrationTest {

  private MemberEntity memberEntity;

  @BeforeEach
  void setUp() {
    memberEntity = memberEntityJpaRepository.save(MemberEntityFixture.create());
  }

  @Test
  @DisplayName("도서 등록 시 20포인트를 지급한다.")
  void creditPointForBookRegistration_Credit20Point_Test() {
    // Given
    long expected = memberEntity.getPoints() + PointService.BOOK_REGISTRATION_POINT;

    // When
    pointService.creditPointForBookRegistration(memberEntity.getMemberId());

    // Then
    long result =
        memberEntityJpaRepository.findById(memberEntity.getMemberId()).orElseThrow().getPoints();

    assertThat(result).isEqualTo(expected);
  }

  @Test
  @DisplayName("서평 등록 시 일반도서라면 15포인트를 지급한다.")
  void creditPointForReviewRegistration_IsNotWeeklyFeaturedBookCredit15Point_Test() {
    // Given
    boolean isWeeklyFeaturedBook = false;
    long expected = memberEntity.getPoints() + PointService.REVIEW_REGISTRATION_BASE_POINT;

    // When
    pointService.creditPointForReviewRegistration(memberEntity.getMemberId(), isWeeklyFeaturedBook);

    // Then
    long result =
        memberEntityJpaRepository.findById(memberEntity.getMemberId()).orElseThrow().getPoints();

    assertThat(result).isEqualTo(expected);
  }

  @Test
  @DisplayName("서평 등록 시 금주도서라면 30포인트를 지급한다.")
  void creditPointForReviewRegistration_IsWeeklyFeaturedBookCredit30Point_Test() {
    // Given
    boolean isWeeklyFeaturedBook = true;
    long expected = memberEntity.getPoints() + PointService.REVIEW_REGISTRATION_EVENT_POINT;

    // When
    pointService.creditPointForReviewRegistration(memberEntity.getMemberId(), isWeeklyFeaturedBook);

    // Then
    long result =
        memberEntityJpaRepository.findById(memberEntity.getMemberId()).orElseThrow().getPoints();

    assertThat(result).isEqualTo(expected);
  }

  @Test
  @DisplayName("서평 삭제 시 15포인트를 차감한다.")
  void debitPointForReviewRemoval_Debit15Point_Test() {
    // Given
    long expected = memberEntity.getPoints() - PointService.REVIEW_REGISTRATION_BASE_POINT;

    // When
    pointService.debitPointForReviewRemoval(memberEntity.getMemberId());

    // Then
    long result =
        memberEntityJpaRepository.findById(memberEntity.getMemberId()).orElseThrow().getPoints();

    assertThat(result).isEqualTo(expected);
  }

  @Test
  @DisplayName("서평픽 시 일일 한계치 이하라면 픽커에게 1포인트, 리시버에게 5포인트 지급하고 픽커의 일일 포인트 지급수를 1 증가시킨다.")
  void creditPointForReviewPick_UnderLimit_CreditPoint_Test() {
    // Given
    MemberEntity picker = memberEntityJpaRepository.save(MemberEntityFixture.create());
    MemberEntity receiver = memberEntityJpaRepository.save(MemberEntityFixture.create());

    long expectedPickerPoint = picker.getPoints() + PointService.REVIEW_PICK_PICKER_POINT;
    long expectedReceiverPoint = receiver.getPoints() + PointService.REVIEW_PICK_RECEIVER_POINT;
    int expectedPickerCreditCount = 1;

    // When
    pointService.creditPointForReviewPick(picker.getMemberId(), receiver.getMemberId());

    // Then
    long pickerPoint =
        memberEntityJpaRepository.findById(picker.getMemberId()).orElseThrow().getPoints();
    long receiverPoint =
        memberEntityJpaRepository.findById(receiver.getMemberId()).orElseThrow().getPoints();
    long pickerCreditCount =
        dailyPointLimitRepository.findById(picker.getMemberId()).orElseThrow().getCreditCount();

    assertThat(pickerPoint).isEqualTo(expectedPickerPoint);
    assertThat(receiverPoint).isEqualTo(expectedReceiverPoint);
    assertThat(pickerCreditCount).isEqualTo(expectedPickerCreditCount);
  }

  @Test
  @DisplayName("서평픽 시 일일 한계치를 넘었다면 포인트를 지급하지 않는다.")
  void creditPointForReviewPick_OverLimit_DoNotCreditPoint_Test() {
    // Given
    MemberEntity picker = memberEntityJpaRepository.save(MemberEntityFixture.create());
    MemberEntity receiver = memberEntityJpaRepository.save(MemberEntityFixture.create());

    long expectedPickerPoint = picker.getPoints();
    long expectedReceiverPoint = receiver.getPoints();
    long expectedPickerCreditCount = 5;

    IntStream.range(0, 5)
        .forEach(i -> dailyPointLimitRepository.increaseCreditCount(picker.getMemberId()));

    // When
    pointService.creditPointForReviewPick(picker.getMemberId(), receiver.getMemberId());

    // Then
    long pickerPoint =
        memberEntityJpaRepository.findById(picker.getMemberId()).orElseThrow().getPoints();
    long receiverPoint =
        memberEntityJpaRepository.findById(receiver.getMemberId()).orElseThrow().getPoints();
    long pickerCreditCount =
        dailyPointLimitRepository.findById(picker.getMemberId()).orElseThrow().getCreditCount();

    assertThat(pickerPoint).isEqualTo(expectedPickerPoint);
    assertThat(receiverPoint).isEqualTo(expectedReceiverPoint);
    assertThat(pickerCreditCount).isEqualTo(expectedPickerCreditCount);
  }

  @Test
  @DisplayName("서평픽 취소 시 픽커에게 1포인트 차감하고, 일일 서평픽 포인트 지급수를 1 감소시킨다.")
  void debitPointForReviewPick_Test() {
    // Given
    MemberEntity picker = memberEntityJpaRepository.save(MemberEntityFixture.create());

    long expectedPoint = picker.getPoints() - PointService.REVIEW_PICK_PICKER_POINT;
    int expectedCount = 2;

    dailyPointLimitRepository.save(new DailyPointLimit(picker.getMemberId(), expectedCount + 1));

    // When
    pointService.debitPointForReviewPick(picker.getMemberId());

    // Then
    long resultPoint =
        memberEntityJpaRepository.findById(picker.getMemberId()).orElseThrow().getPoints();
    long resultCount =
        dailyPointLimitRepository.findById(picker.getMemberId()).orElseThrow().getCreditCount();

    assertThat(resultPoint).isEqualTo(expectedPoint);
    assertThat(resultCount).isEqualTo(expectedCount);
  }

  @Test
  @DisplayName("오늘 서평픽한 적이 없다면 포인트와 잔여횟수가 차감되지 않는다.")
  void debitPointForReviewPick_IfZeroCreditCount_Test() {
    // Given
    MemberEntity picker = memberEntityJpaRepository.save(MemberEntityFixture.create());

    dailyPointLimitRepository.save(new DailyPointLimit(picker.getMemberId(), 0));

    long expectedPoint = picker.getPoints();
    long expectedCount = 0;

    // When
    pointService.debitPointForReviewPick(picker.getMemberId());

    // Then
    long resultPoint =
        memberEntityJpaRepository.findById(picker.getMemberId()).orElseThrow().getPoints();
    long resultCount =
        dailyPointLimitRepository.findById(picker.getMemberId()).orElseThrow().getCreditCount();

    assertThat(resultPoint).isEqualTo(expectedPoint);
    assertThat(resultCount).isEqualTo(expectedCount);
  }

  @Test
  @DisplayName("누적 출석일이 없는 경우 기본 1포인트만 지급한다.")
  void creditPointForAttendance_Daily1Point_Test() {
    // Given
    long expectedPoint = memberEntity.getPoints() + ATTENDANCE_DAILY_POINT;

    // When
    pointService.creditPointForAttendance(memberEntity.getMemberId(), 1);

    // Then
    long result =
        memberEntityJpaRepository.findById(memberEntity.getMemberId()).orElseThrow().getPoints();

    assertThat(result).isEqualTo(expectedPoint);
  }

  @Test
  @DisplayName("3일째 출석 시 3포인트를 지급한다.")
  void creditPointForAttendance_3Days_Test() {
    // Given
    long expectedPoint =
        memberEntity.getPoints() + ATTENDANCE_DAILY_POINT + ATTENDANCE_3_DAYS_POINT;

    // When
    pointService.creditPointForAttendance(
        memberEntity.getMemberId(), (int) ATTENDANCE_3_DAYS_POINT);

    // Then
    long result =
        memberEntityJpaRepository.findById(memberEntity.getMemberId()).orElseThrow().getPoints();

    assertThat(result).isEqualTo(expectedPoint);
  }

  @Test
  @DisplayName("5일째 출석 시 5포인트를 지급한다.")
  void creditPointForAttendance_5Days_Test() {
    // Given
    long expectedPoint =
        memberEntity.getPoints() + ATTENDANCE_DAILY_POINT + ATTENDANCE_5_DAYS_POINT;

    // When
    pointService.creditPointForAttendance(
        memberEntity.getMemberId(), (int) ATTENDANCE_5_DAYS_POINT);

    // Then
    long result =
        memberEntityJpaRepository.findById(memberEntity.getMemberId()).orElseThrow().getPoints();

    assertThat(result).isEqualTo(expectedPoint);
  }

  @Test
  @DisplayName("10일째 출석 시 10포인트를 지급한다.")
  void creditPointForAttendance_10Days_Test() {
    // Given
    long expectedPoint =
        memberEntity.getPoints() + ATTENDANCE_DAILY_POINT + ATTENDANCE_10_DAYS_POINT;

    // When
    pointService.creditPointForAttendance(
        memberEntity.getMemberId(), (int) ATTENDANCE_10_DAYS_POINT);

    // Then
    long result =
        memberEntityJpaRepository.findById(memberEntity.getMemberId()).orElseThrow().getPoints();

    assertThat(result).isEqualTo(expectedPoint);
  }

  @Test
  @DisplayName("30일째 출석 시 30포인트를 지급한다.")
  void creditPointForAttendance_30Days_Test() {
    // Given
    long expectedPoint =
        memberEntity.getPoints() + ATTENDANCE_DAILY_POINT + ATTENDANCE_30_DAYS_POINT;

    // When
    pointService.creditPointForAttendance(
        memberEntity.getMemberId(), (int) ATTENDANCE_30_DAYS_POINT);

    // Then
    long result =
        memberEntityJpaRepository.findById(memberEntity.getMemberId()).orElseThrow().getPoints();

    assertThat(result).isEqualTo(expectedPoint);
  }
}
