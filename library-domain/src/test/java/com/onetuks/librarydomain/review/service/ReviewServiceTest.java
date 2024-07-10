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
import com.onetuks.librarydomain.review.service.dto.param.ReviewParam;
import com.onetuks.libraryobject.enums.RoleType;
import com.onetuks.libraryobject.exception.ApiAccessDeniedException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReviewServiceTest extends DomainIntegrationTest {

  @Test
  @DisplayName("서평을 등록하면 금주도서인 경우 30P, 그 외 15P가 지급된다.")
  void register_CreditPointByBook_Test() {
    // Given
    Review review =
        ReviewFixture.create(
            101L, MemberFixture.create(101L, RoleType.USER), BookFixture.create(101L));
    ReviewParam param = new ReviewParam("서평제목", "서평본문");

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

  @Test
  @DisplayName("서평을 수정한다.")
  void edit_Test() {
    // Given
    Review before =
        ReviewFixture.create(
            102L, MemberFixture.create(102L, RoleType.USER), BookFixture.create(102L));
    ReviewParam param = new ReviewParam("수정된 서평제목", "수정된 서평본문");
    Review after =
        new Review(
            before.reviewId(),
            before.member(),
            before.book(),
            param.reviewTitle(),
            param.reviewContent(),
            before.pickCount(),
            before.createdAt(),
            LocalDateTime.now());

    given(reviewRepository.read(before.reviewId())).willReturn(before);
    given(reviewRepository.update(any(Review.class))).willReturn(after);

    // When
    Review result = reviewService.edit(before.member().memberId(), before.reviewId(), param);

    // Then
    assertAll(
        () -> assertThat(result.reviewId()).isPositive(),
        () -> assertThat(result.member()).isEqualTo(after.member()),
        () -> assertThat(result.book()).isEqualTo(after.book()),
        () -> assertThat(result.reviewTitle()).contains("수정된"),
        () -> assertThat(result.reviewContent()).contains("수정된"),
        () -> assertThat(result.pickCount()).isZero(),
        () -> assertThat(result.createdAt()).isEqualTo(before.createdAt()),
        () -> assertThat(result.updatedAt()).isAfter(before.updatedAt()));
  }

  @Test
  @DisplayName("해당 서평에 대한 권한이 없는 멤버가 서평 수정 시 예외가 발생한다.")
  void edit_NotAuth_Exception() {
    // Given
    long notAuthLoginId = 1L;
    Review review =
        ReviewFixture.create(
            102L, MemberFixture.create(102L, RoleType.USER), BookFixture.create(102L));
    ReviewParam param = new ReviewParam("수정된 서평제목", "수정된 서평본문");

    given(reviewRepository.read(review.reviewId())).willReturn(review);

    // When & Then
    assertThatThrownBy(() -> reviewService.edit(notAuthLoginId, review.reviewId(), param))
        .isInstanceOf(ApiAccessDeniedException.class);
  }
}
