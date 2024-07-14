package com.onetuks.librarypoint.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.onetuks.dbstorage.member.entity.MemberEntity;
import com.onetuks.librarypoint.CorePointIntegrationTest;
import com.onetuks.librarypoint.fixture.MemberEntityFixture;
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
    long expected = memberEntity.getPoints() + 20L;

    // When
    pointService.creditPointForBookRegistration(memberEntity.getMemberId());

    // Then
    long result =
        memberEntityJpaRepository.findById(memberEntity.getMemberId()).orElseThrow().getPoints();

    assertThat(result).isEqualTo(expected);
  }

  @Test
  @DisplayName("서평 등록 시 일반도서라면 15포인트를 지급한다.")
  void creditPointForReviewRegistration_Credit15Point_Test() {
    // Given
    long expected = memberEntity.getPoints() + 15L;

    // When
    pointService.creditPointForReviewRegistration(memberEntity.getMemberId());

    // Then
    long result =
        memberEntityJpaRepository.findById(memberEntity.getMemberId()).orElseThrow().getPoints();

    assertThat(result).isEqualTo(expected);
  }

  @Test
  @DisplayName("서평 삭제 시 15포인트를 차감한다.")
  void debitPointForReviewRemoval_Debit15Point_Test() {
    // Given
    long expected = memberEntity.getPoints() - 15L;

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

    long expectedPickerPoint = picker.getPoints() + 1L;
    long expectedReceiverPoint = receiver.getPoints() + 5L;
    int expectedPickerCreditCount = 1;

    // When
    pointService.creditPointForReviewPick(picker.getMemberId(), receiver.getMemberId());

    // Then
    long pickerPoint =
        memberEntityJpaRepository.findById(picker.getMemberId()).orElseThrow().getPoints();
    long receiverPoint =
        memberEntityJpaRepository.findById(receiver.getMemberId()).orElseThrow().getPoints();
    long pickerCreditCount = dailyPointLimitRepository.find(picker.getMemberId());

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

    dailyPointLimitRepository.save(picker.getMemberId(), 5);

    // When
    pointService.creditPointForReviewPick(picker.getMemberId(), receiver.getMemberId());

    // Then
    long pickerPoint =
        memberEntityJpaRepository.findById(picker.getMemberId()).orElseThrow().getPoints();
    long receiverPoint =
        memberEntityJpaRepository.findById(receiver.getMemberId()).orElseThrow().getPoints();
    long pickerCreditCount = dailyPointLimitRepository.find(picker.getMemberId());

    assertThat(pickerPoint).isEqualTo(expectedPickerPoint);
    assertThat(receiverPoint).isEqualTo(expectedReceiverPoint);
    assertThat(pickerCreditCount).isEqualTo(expectedPickerCreditCount);
  }

  @Test
  @DisplayName("서평픽 취소 시 픽커에게 1포인트 차감하고, 일일 서평픽 포인트 지급수를 1 감소시킨다.")
  void debitPointForReviewPick() {
    // Given
    long expectedPoint = memberEntity.getPoints() - 1L;
    int expectedCount = -1;

    // When
    pointService.debitPointForReviewPick(memberEntity.getMemberId());

    // Then
    long resultPoint =
        memberEntityJpaRepository.findById(memberEntity.getMemberId()).orElseThrow().getPoints();
    long resultCount = dailyPointLimitRepository.find(memberEntity.getMemberId());

    assertThat(resultPoint).isEqualTo(expectedPoint);
    assertThat(resultCount).isEqualTo(expectedCount);
  }
}
