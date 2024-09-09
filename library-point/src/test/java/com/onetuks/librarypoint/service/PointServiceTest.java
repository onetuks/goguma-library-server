package com.onetuks.librarypoint.service;

import static com.onetuks.librarypoint.service.model.vo.Activity.ATTENDANCE_10_DAYS;
import static com.onetuks.librarypoint.service.model.vo.Activity.ATTENDANCE_DAILY;
import static org.assertj.core.api.Assertions.assertThat;

import com.onetuks.dbstorage.member.entity.MemberEntity;
import com.onetuks.librarypoint.CorePointIntegrationTest;
import com.onetuks.librarypoint.fixture.MemberEntityFixture;
import com.onetuks.librarypoint.repository.entity.DailyPointLimit;
import com.onetuks.librarypoint.service.model.PointHistory;
import com.onetuks.librarypoint.service.model.vo.Activity;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class PointServiceTest extends CorePointIntegrationTest {

  private MemberEntity memberEntity;

  @BeforeEach
  void setUp() {
    memberEntity = memberEntityJpaRepository.save(MemberEntityFixture.create());
  }

  @Test
  @DisplayName("도서 등록 시 20포인트를 지급한다.")
  void creditPointForBookRegistration_Credit20Point_Test() {
    // Given
    long expected = memberEntity.getPoints() + Activity.BOOK_REGISTRATION.getPoints();

    // When
    pointService.creditPointForBookRegistration(memberEntity.getMemberId());

    // Then
    long result =
        memberEntityJpaRepository.findById(memberEntity.getMemberId()).orElseThrow().getPoints();

    assertThat(result).isEqualTo(expected);
  }

  @Test
  @DisplayName("도서 등록이 취소되면 20포인트를 차감한다.")
  void debitPointForBookRemoval_Debit20Point_Test() {
    // Given
    long expected = memberEntity.getPoints() + Activity.BOOK_REGISTRATION.getNegativePoints();

    // When
    pointService.debitPointForBookRemoval(memberEntity.getMemberId());

    // Then
    long result =
        memberEntityJpaRepository.findById(memberEntity.getMemberId()).orElseThrow().getPoints();

    assertThat(result).isEqualTo(expected);
  }

  @Test
  @DisplayName("서평 등록 시 일반도서라면 15포인트를 지급한다.")
  void creditPointForReviewRegistration_IsNotWeeklyFeaturedBookCredit15Point_Test() {
    // Given
    long expected = memberEntity.getPoints() + Activity.REVIEW_REGISTRATION_BASE.getPoints();

    // When
    pointService.creditPointForReviewRegistration(memberEntity.getMemberId());

    // Then
    long result =
        memberEntityJpaRepository.findById(memberEntity.getMemberId()).orElseThrow().getPoints();

    assertThat(result).isEqualTo(expected);
  }

  @Test
  @DisplayName("서평 등록 시 금주도서라면 30포인트를 지급한다.")
  void creditPointForReviewRegistration_IsWeeklyFeaturedBookCredit30Point_Test() {
    // Given
    long expected = memberEntity.getPoints() + Activity.REVIEW_REGISTRATION_EVENT.getPoints();

    // When
    pointService.creditPointForReviewRegistrationEvent(memberEntity.getMemberId());

    // Then
    long result =
        memberEntityJpaRepository.findById(memberEntity.getMemberId()).orElseThrow().getPoints();

    assertThat(result).isEqualTo(expected);
  }

  @Test
  @DisplayName("서평 삭제 시 15포인트를 차감한다.")
  void debitPointForReviewRemoval_Debit15Point_Test() {
    // Given
    long expected =
        memberEntity.getPoints() + Activity.REVIEW_REGISTRATION_BASE.getNegativePoints();

    // When
    pointService.debitPointForReviewRemoval(memberEntity.getMemberId());

    // Then
    long result =
        memberEntityJpaRepository.findById(memberEntity.getMemberId()).orElseThrow().getPoints();

    assertThat(result).isEqualTo(expected);
  }

  @Test
  @DisplayName("서평픽 시 일일 한계치 이하라면, 픽커에게 1포인트 지급하고 일일 포인트 지급수를 1 증가시킨다.")
  void creditPointForReviewPicker_UnderLimit_CreditPoint_Test() {
    // Given
    MemberEntity picker = memberEntityJpaRepository.save(MemberEntityFixture.create());

    long expectedPickerPoint = picker.getPoints() + Activity.REVIEW_PICK_PICKER.getPoints();
    int expectedPickerCreditCount = 1;

    // When
    pointService.creditPointForReviewPicker(picker.getMemberId());

    // Then
    long pickerPoint =
        memberEntityJpaRepository.findById(picker.getMemberId()).orElseThrow().getPoints();
    long pickerCreditCount =
        dailyPointLimitRepository.findById(picker.getMemberId()).orElseThrow().getCreditCount();

    assertThat(pickerPoint).isEqualTo(expectedPickerPoint);
    assertThat(pickerCreditCount).isEqualTo(expectedPickerCreditCount);
  }

  @Test
  @DisplayName("서평픽 시 일일 한계치를 넘었다면 포인트를 지급하지 않는다.")
  void creditPointForReviewPick_OverLimit_DoNotCreditPoint_Test() {
    // Given
    MemberEntity picker = memberEntityJpaRepository.save(MemberEntityFixture.create());

    long expectedPickerPoint = picker.getPoints();
    long expectedPickerCreditCount = 5;

    IntStream.range(0, 5)
        .forEach(i -> dailyPointLimitRepository.increaseCreditCount(picker.getMemberId()));

    // When
    pointService.creditPointForReviewPicker(picker.getMemberId());

    // Then
    long pickerPoint =
        memberEntityJpaRepository.findById(picker.getMemberId()).orElseThrow().getPoints();
    long pickerCreditCount =
        dailyPointLimitRepository.findById(picker.getMemberId()).orElseThrow().getCreditCount();

    assertThat(pickerPoint).isEqualTo(expectedPickerPoint);
    assertThat(pickerCreditCount).isEqualTo(expectedPickerCreditCount);
  }

  @Test
  @DisplayName("서평픽 등록 시 리시버에게 5포인트 지급한다")
  void creditPointForReviewReceiver_CreditPoint_Test() {
    // Given
    MemberEntity receiver = memberEntityJpaRepository.save(MemberEntityFixture.create());

    long expectedReceiverPoint = receiver.getPoints() + Activity.REVIEW_PICK_RECEIVER.getPoints();

    // When
    pointService.creditPointForReviewReceiver(receiver.getMemberId());

    // Then
    long receiverPoint =
        memberEntityJpaRepository.findById(receiver.getMemberId()).orElseThrow().getPoints();

    assertThat(receiverPoint).isEqualTo(expectedReceiverPoint);
  }

  //  @Test
  //  @DisplayName("여러 픽커가 한 명의 리시버의 서평을 픽했을때 리시버에게 각 픽당 5포인트가 모두 지급된다.")
  //  void creditPointForReviewReceiver_MultiReviewPick_CreditAllPointsTest()
  //      throws InterruptedException {
  //    // Given
  //    int threadCount = 5;
  //    long memberId = memberEntityJpaRepository.save(MemberEntityFixture.create()).getMemberId();
  //    ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
  //    CountDownLatch latch = new CountDownLatch(1);
  //
  //    // When
  //    Runnable task = () -> {
  //      try {
  //        latch.await();
  //        pointService.creditPointForReviewReceiver(memberId);
  //      } catch (Exception e) {
  //        log.warn("Error occurred while executing task", e);
  //      }
  //    };
  //
  //    for (int i = 0; i < threadCount; i++) {
  //      executorService.submit(task);
  //    }
  //
  //    latch.countDown();
  //    executorService.shutdown();
  //    executorService.awaitTermination(10, TimeUnit.SECONDS);
  //
  //    // Then
  //    MemberEntity memberEntity =
  // memberEntityJpaRepository.findByMemberId(memberId).orElseThrow();
  //
  //    assertThat(memberEntity.getPoints()).isEqualTo(5 * threadCount);
  //  }

  @Test
  @DisplayName("서평픽 취소 시 픽커에게 1포인트 차감하고, 일일 서평픽 포인트 지급수를 1 감소시킨다.")
  void debitPointForReviewPicker_Test() {
    // Given
    MemberEntity picker = memberEntityJpaRepository.save(MemberEntityFixture.create());

    long expectedPoint = picker.getPoints() + Activity.REVIEW_PICK_PICKER.getNegativePoints();
    int expectedCount = 2;

    dailyPointLimitRepository.save(new DailyPointLimit(picker.getMemberId(), expectedCount + 1));

    // When
    pointService.debitPointForReviewPicker(picker.getMemberId());

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
  void debitPointForReviewPicker_IfZeroCreditCount_Test() {
    // Given
    MemberEntity picker = memberEntityJpaRepository.save(MemberEntityFixture.create());

    dailyPointLimitRepository.save(new DailyPointLimit(picker.getMemberId(), 0));

    long expectedPoint = picker.getPoints();
    long expectedCount = 0;

    // When
    pointService.debitPointForReviewPicker(picker.getMemberId());

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
    Activity activity = ATTENDANCE_DAILY;
    long expectedPoint = memberEntity.getPoints() + activity.getPoints();

    // When
    pointService.creditPointForAttendance(memberEntity.getMemberId(), activity);

    // Then
    long result =
        memberEntityJpaRepository.findById(memberEntity.getMemberId()).orElseThrow().getPoints();

    assertThat(result).isEqualTo(expectedPoint);
  }

  @Test
  @DisplayName("(n > 1)일째 출석 시 (1 + n) 포인트를 지급한다.")
  void creditPointForAttendance_nDays_CreditN1PointsTest() {
    // Given
    Activity activity = ATTENDANCE_10_DAYS;
    long expectedPoint =
        memberEntity.getPoints() + ATTENDANCE_DAILY.getPoints() + activity.getPoints();

    // When
    pointService.creditPointForAttendance(memberEntity.getMemberId(), activity);

    // Then
    long result =
        memberEntityJpaRepository.findById(memberEntity.getMemberId()).orElseThrow().getPoints();

    assertThat(result).isEqualTo(expectedPoint);
  }

  @Test
  @DisplayName("모든 포인트 내역을 조회한다.")
  void searchAllPointHistory_Test() {
    // Given
    Pageable pageable = PageRequest.of(0, 10);
    MemberEntity member = memberEntityJpaRepository.save(MemberEntityFixture.create());
    IntStream.range(0, pageable.getPageSize())
        .forEach(i -> pointService.creditPointForBookRegistration(member.getMemberId()));

    // When
    Page<PointHistory> results =
        pointService.searchAllPointHistories(member.getMemberId(), pageable);

    // Then
    assertThat(results).hasSize(pageable.getPageSize());
  }
}
