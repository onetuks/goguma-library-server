package com.onetuks.librarydomain.review.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;

import com.onetuks.librarydomain.BookFixture;
import com.onetuks.librarydomain.DomainIntegrationTest;
import com.onetuks.librarydomain.MemberFixture;
import com.onetuks.librarydomain.ReviewFixture;
import com.onetuks.librarydomain.ReviewPickFixture;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.review.model.ReviewPick;
import com.onetuks.libraryobject.enums.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReviewPickServiceTest extends DomainIntegrationTest {

  @Test
  @DisplayName("서평픽 등록하면 픽커에게 1포인트, 리시버에게 5포인트 지급한다.")
  void register_CreditPointUnderLimit_Test() {
    // Given
    Member picker = MemberFixture.create(101L, RoleType.USER);
    Member receiver = MemberFixture.create(201L, RoleType.USER);
    ReviewPick reviewPick =
        ReviewPickFixture.create(
            101L, picker, ReviewFixture.create(101L, receiver, BookFixture.create(101L)));

    given(memberRepository.read(picker.memberId())).willReturn(picker);
    given(reviewRepository.read(reviewPick.review().reviewId())).willReturn(reviewPick.review());
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
}
