package com.onetuks.librarydomain.review.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;

import com.onetuks.librarydomain.BookFixture;
import com.onetuks.librarydomain.DomainIntegrationTest;
import com.onetuks.librarydomain.MemberFixture;
import com.onetuks.librarydomain.ReviewFixture;
import com.onetuks.librarydomain.book.model.Book;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.review.model.Review;
import com.onetuks.librarydomain.review.service.dto.param.ReviewParam;
import com.onetuks.librarydomain.weekly.model.WeeklyFeaturedBook;
import com.onetuks.libraryobject.enums.Category;
import com.onetuks.libraryobject.enums.RoleType;
import com.onetuks.libraryobject.enums.SortBy;
import com.onetuks.libraryobject.exception.ApiAccessDeniedException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class ReviewServiceTest extends DomainIntegrationTest {

  @Test
  @DisplayName("서평을 등록하면 포인트가 지급되며, 서평 카테고리 통계가 업데이트된다.")
  void register_CreditPointByBook_Test() {
    // Given
    Review beforeReview =
        ReviewFixture.create(
            101L, MemberFixture.create(101L, RoleType.USER), BookFixture.create(101L));
    ReviewParam param = new ReviewParam("서평제목", "서평본문");
    Member picker =
        beforeReview.member().increaseReviewCategoryStatics(beforeReview.book().categories());
    Page<WeeklyFeaturedBook> weeklyFeaturedBooks =
        new PageImpl<>(
            IntStream.range(0, 3)
                .mapToObj(i -> WeeklyFeaturedBook.of(beforeReview.book()))
                .toList());
    Review afterReview =
        new Review(
            beforeReview.reviewId(),
            picker,
            beforeReview.book(),
            beforeReview.reviewTitle(),
            beforeReview.reviewContent(),
            beforeReview.pickCount(),
            beforeReview.createdAt(),
            beforeReview.updatedAt());

    given(memberRepository.read(beforeReview.member().memberId()))
        .willReturn(beforeReview.member());
    given(bookRepository.read(beforeReview.book().bookId())).willReturn(beforeReview.book());
    given(memberRepository.update(any(Member.class))).willReturn(picker);
    given(reviewRepository.create(any(Review.class))).willReturn(afterReview);
    given(weeklyFeaturedBookRepository.readAllForThisWeek()).willReturn(weeklyFeaturedBooks);

    // When
    Review result = reviewService.register(picker.memberId(), beforeReview.book().bookId(), param);

    // Then
    Map<Category, Long> afterMemberStatics = result.member().memberStatics().reviewCategoryCounts();
    Map<Category, Long> beforeMemberStatics =
        beforeReview.member().memberStatics().reviewCategoryCounts();

    assertAll(
        () -> assertThat(result.reviewId()).isPositive(),
        () -> assertThat(result.member()).isEqualTo(picker),
        () -> assertThat(result.book()).isEqualTo(beforeReview.book()),
        () -> assertThat(result.reviewTitle()).isEqualTo(beforeReview.reviewTitle()),
        () -> assertThat(result.reviewContent()).isEqualTo(beforeReview.reviewContent()),
        () -> assertThat(result.pickCount()).isZero(),
        () -> assertThat(result.createdAt()).isNotNull(),
        () -> assertThat(result.updatedAt()).isNotNull());

    afterMemberStatics.forEach(
        (category, count) -> {
          if (beforeReview.book().categories().contains(category)) {
            assertThat(count).isEqualTo(beforeMemberStatics.get(category) + 1);
          } else {
            assertThat(count).isEqualTo(beforeMemberStatics.get(category));
          }
        });

    verify(pointService, times(1)).creditPointForReviewRegistration(picker.memberId(), true);
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

  @Test
  @DisplayName("서평을 삭제하면 포인트가 15P 차감되고, 멤버 통계 정보가 업데이트된다.")
  void remove_DebitPoint_Test() {
    // Given
    Member picker = MemberFixture.create(102L, RoleType.USER);
    Review review = ReviewFixture.create(102L, picker, BookFixture.create(102L));

    given(reviewRepository.read(review.reviewId())).willReturn(review);

    // When
    reviewService.remove(picker.memberId(), review.reviewId());

    // Then
    verify(pointService, times(1)).debitPointForReviewRemoval(picker.memberId());
    verify(reviewRepository, times(1)).delete(review.reviewId());
  }

  @Test
  @DisplayName("서평을 조회한다.")
  void search_Test() {
    // Given
    Review review =
        ReviewFixture.create(
            103L, MemberFixture.create(103L, RoleType.USER), BookFixture.create(103L));

    given(reviewRepository.read(review.reviewId())).willReturn(review);

    // When
    Review result = reviewService.search(review.reviewId());

    // Then
    assertThat(result).isEqualTo(review);
  }

  @Test
  @DisplayName("정렬 기준에 따라 서평 목록을 조회한다.")
  void searchAll_Test() {
    // Given
    Pageable pageable = PageRequest.of(0, 10);
    SortBy sortBy = SortBy.PICK;
    Page<Review> reviews =
        new PageImpl<>(
            IntStream.range(0, 10)
                .mapToObj(
                    i ->
                        ReviewFixture.create(
                            (long) i,
                            MemberFixture.create((long) i, RoleType.USER),
                            BookFixture.create((long) i)))
                .toList());

    given(reviewRepository.readAll(sortBy, pageable)).willReturn(reviews);

    // When
    Page<Review> results = reviewService.searchAll(sortBy, pageable);

    // Then
    assertThat(results).hasSize((int) reviews.getTotalElements());
  }

  @Test
  @DisplayName("도서에 대한 모든 서평을 조회한다.")
  void searchAll_OfBook_Test() {
    // Given
    Pageable pageable = PageRequest.of(0, 10);
    SortBy sortBy = SortBy.PICK;
    Book book = BookFixture.create(123L);
    Page<Review> reviews =
        new PageImpl<>(
            IntStream.range(0, 10)
                .mapToObj(
                    i ->
                        ReviewFixture.create(
                            (long) i, MemberFixture.create((long) i, RoleType.USER), book))
                .toList());

    given(reviewRepository.readAll(book.bookId(), sortBy, pageable)).willReturn(reviews);

    // When
    Page<Review> results = reviewService.searchAll(book.bookId(), sortBy, pageable);

    // Then
    assertThat(results)
        .hasSize((int) reviews.getTotalElements())
        .allSatisfy(result -> assertThat(result.book()).isEqualTo(book));
  }

  @Test
  @DisplayName("멤버의 모든 서평을 조회한다.")
  void searchAll_OfMember_Test() {
    // Given
    Pageable pageable = PageRequest.of(0, 10);
    Member member = MemberFixture.create(125L, RoleType.USER);
    Page<Review> reviews =
        new PageImpl<>(
            IntStream.range(0, 10)
                .mapToObj(i -> ReviewFixture.create((long) i, member, BookFixture.create((long) i)))
                .toList());

    given(reviewRepository.readAll(member.memberId(), pageable)).willReturn(reviews);

    // When
    Page<Review> results = reviewService.searchAll(member.memberId(), pageable);

    // Then
    assertThat(results)
        .hasSize((int) reviews.getTotalElements())
        .allSatisfy(result -> assertThat(result.member()).isEqualTo(member));
  }
}
