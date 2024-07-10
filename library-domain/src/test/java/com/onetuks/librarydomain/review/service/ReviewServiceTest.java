package com.onetuks.librarydomain.review.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import com.onetuks.librarydomain.BookFixture;
import com.onetuks.librarydomain.DomainIntegrationTest;
import com.onetuks.librarydomain.MemberFixture;
import com.onetuks.librarydomain.ReviewFixture;
import com.onetuks.librarydomain.review.model.Review;
import com.onetuks.librarydomain.review.service.dto.param.ReviewPostParam;
import com.onetuks.libraryobject.enums.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReviewServiceTest extends DomainIntegrationTest {

  @Test
  @DisplayName("서평을 등록하면 금주도서인 경우 30P, 그 외 15P가 지급된다.")
  void register() {
    // Given
    Review review =
        ReviewFixture.create(
            101L, MemberFixture.create(101L, RoleType.USER), BookFixture.create(101L));
    ReviewPostParam param = new ReviewPostParam("서평제목", "서평본문");

    given(memberRepository.read(review.member().memberId())).willReturn(review.member());
    given(bookRepository.read(review.book().bookId())).willReturn(review.book());
    given(reviewRepository.create(any(Review.class))).willReturn(review);

    // When
    Review result =
        reviewService.register(review.member().memberId(), review.book().bookId(), param);

    // Then
    assertAll(
        () -> assertThat(result.reviewId()).isPositive(),
        () -> assertThat(result.member()).isEqualTo(review.member()),
        () -> assertThat(result.book()).isEqualTo(review.book()),
        () -> assertThat(result.reviewTitle()).isEqualTo(review.reviewTitle()),
        () -> assertThat(result.reviewContent()).isEqualTo(review.reviewContent()),
        () -> assertThat(result.pickCount()).isZero(),
        () -> assertThat(result.createdAt()).isNotNull(),
        () -> assertThat(result.updatedAt()).isNotNull());
  }
}
