package com.onetuks.librarydomain.review.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.never;

import com.onetuks.librarydomain.BookFixture;
import com.onetuks.librarydomain.DomainIntegrationTest;
import com.onetuks.librarydomain.MemberFixture;
import com.onetuks.librarydomain.ReviewFixture;
import com.onetuks.librarydomain.ReviewPickFixture;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.review.model.Review;
import com.onetuks.librarydomain.review.model.ReviewPick;
import com.onetuks.libraryobject.enums.RoleType;
import com.onetuks.libraryobject.exception.ApiAccessDeniedException;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class ReviewPickServiceTest extends DomainIntegrationTest {

  @Test
  @DisplayName("서평픽 등록하면 픽커와 리시버에게 포인트를 지급한다.")
  void register_CreditPoint_Test() {
    // Given
    Member picker = MemberFixture.create(101L, RoleType.USER);
    Member receiver = MemberFixture.create(201L, RoleType.USER);
    ReviewPick reviewPick =
        ReviewPickFixture.create(
            101L, picker, ReviewFixture.create(101L, receiver, BookFixture.create(101L, picker)));

    given(memberRepository.read(picker.memberId())).willReturn(picker);
    given(reviewRepository.readWithLock(reviewPick.review().reviewId()))
        .willReturn(reviewPick.review());
    given(reviewPickRepository.create(any(ReviewPick.class))).willReturn(reviewPick);

    // When
    ReviewPick result =
        reviewPickService.register(picker.memberId(), reviewPick.review().reviewId());

    // Then
    assertAll(
        () -> assertThat(result.reviewPickId()).isEqualTo(reviewPick.reviewPickId()),
        () -> assertThat(result.member().memberId()).isEqualTo(reviewPick.member().memberId()),
        () -> assertThat(result.review().reviewId()).isEqualTo(reviewPick.review().reviewId()));

    verify(pointService, times(1)).creditPointForReviewPick(picker.memberId(), receiver.memberId());
  }

  @Test
  @DisplayName("서평픽 취소하면 서평픽이 취소되고, 픽커의 포인트를 차감한다.")
  void remove_DebitPoint_Test() {
    // Given
    Member picker = MemberFixture.create(103L, RoleType.USER);
    ReviewPick reviewPick =
        ReviewPickFixture.create(
            103L,
            picker,
            ReviewFixture.create(
                103L, MemberFixture.create(203L, RoleType.USER), BookFixture.create(103L, picker)));

    given(memberRepository.read(picker.memberId())).willReturn(picker);
    given(reviewPickRepository.read(reviewPick.reviewPickId())).willReturn(reviewPick);

    // When
    reviewPickService.remove(picker.memberId(), reviewPick.reviewPickId());

    // Then
    verify(reviewPickRepository, times(1)).delete(reviewPick.reviewPickId());
    verify(pointService, times(1)).debitPointForReviewPick(picker.memberId());
  }

  @Test
  @DisplayName("권한 없는 멤버가 서평픽 취소하면 에외를 던진다.")
  void remove_AccessDenied_ExceptionThrown() {
    // Given
    Member notPicker = MemberFixture.create(103L, RoleType.USER);
    ReviewPick reviewPick =
        ReviewPickFixture.create(
            103L,
            MemberFixture.create(303L, RoleType.USER),
            ReviewFixture.create(
                103L,
                MemberFixture.create(203L, RoleType.USER),
                BookFixture.create(103L, notPicker)));

    given(memberRepository.read(notPicker.memberId())).willReturn(notPicker);
    given(reviewPickRepository.read(reviewPick.reviewPickId())).willReturn(reviewPick);

    // When & Then
    assertThatThrownBy(
            () -> reviewPickService.remove(notPicker.memberId(), reviewPick.reviewPickId()))
        .isInstanceOf(ApiAccessDeniedException.class);

    verify(reviewPickRepository, never()).delete(reviewPick.reviewPickId());
    verify(pointService, never()).debitPointForReviewPick(notPicker.memberId());
  }

  @Test
  @DisplayName("해당 유저의 모든 서평픽을 조회한다.")
  void searchAll_Test() {
    // Given
    Pageable pageable = PageRequest.of(0, 10);
    Member picker = MemberFixture.create(104L, RoleType.USER);
    Page<ReviewPick> reviewPicks =
        new PageImpl<>(
            IntStream.range(0, pageable.getPageSize())
                .mapToObj(
                    i ->
                        ReviewPickFixture.create(
                            103L,
                            picker,
                            ReviewFixture.create(
                                103L,
                                MemberFixture.create(203L, RoleType.USER),
                                BookFixture.create(103L, picker))))
                .toList());

    given(reviewPickRepository.readAll(picker.memberId(), pageable)).willReturn(reviewPicks);

    // When
    Page<ReviewPick> results = reviewPickService.searchAll(picker.memberId(), pageable);

    // Then
    assertThat(results)
        .hasSize(pageable.getPageSize())
        .allSatisfy(result -> assertThat(result.member().memberId()).isEqualTo(picker.memberId()));
  }

  @Test
  @DisplayName("해당 유저가 해당 서평을 픽했는지 조회한다.")
  void searchExistence_Test() {
    // Given
    Member picker = MemberFixture.create(104L, RoleType.USER);
    ReviewPick reviewPick =
        ReviewPickFixture.create(
            104L,
            picker,
            ReviewFixture.create(
                104L, MemberFixture.create(204L, RoleType.USER), BookFixture.create(104L, picker)));

    given(reviewPickRepository.read(picker.memberId(), reviewPick.review().reviewId()))
        .willReturn(reviewPick);

    // When
    ReviewPick result =
        reviewPickService.searchExistence(picker.memberId(), reviewPick.review().reviewId());

    // Then
    assertAll(
        () -> assertThat(result.reviewPickId()).isNotNull(),
        () -> assertThat(result.member().memberId()).isEqualTo(picker.memberId()),
        () -> assertThat(result.review().reviewId()).isEqualTo(reviewPick.review().reviewId()));
  }

  @Test
  @DisplayName("해당 서평의 서평픽 개수를 조회한다.")
  void searchCount_Test() {
    // Given
    Member member = MemberFixture.create(105L, RoleType.USER);
    Review review = ReviewFixture.create(105L, member, BookFixture.create(105L, member));

    given(reviewPickRepository.readCount(member.memberId(), review.reviewId())).willReturn(1L);

    // When
    Long result = reviewPickService.searchCount(member.memberId(), review.reviewId());

    // Then
    assertThat(result).isOne();
  }
}
