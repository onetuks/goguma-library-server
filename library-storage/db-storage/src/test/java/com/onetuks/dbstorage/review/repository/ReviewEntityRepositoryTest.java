package com.onetuks.dbstorage.review.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.dbstorage.DbStorageIntegrationTest;
import com.onetuks.librarydomain.BookFixture;
import com.onetuks.librarydomain.MemberFixture;
import com.onetuks.librarydomain.ReviewFixture;
import com.onetuks.librarydomain.book.model.Book;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.review.model.Review;
import com.onetuks.libraryobject.enums.RoleType;
import com.onetuks.libraryobject.enums.SortBy;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

class ReviewEntityRepositoryTest extends DbStorageIntegrationTest {

  @Test
  @DisplayName("서평을 등록한다.")
  void create() {
    // Given
    Review review =
        ReviewFixture.create(
            null,
            memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
            bookEntityRepository.create(BookFixture.create(null)));

    // When
    Review result = reviewEntityRepository.create(review);

    // Then
    assertAll(
        () -> assertThat(result.reviewId()).isPositive(),
        () -> assertThat(result.member()).isEqualTo(review.member()),
        () -> assertThat(result.book()).isEqualTo(review.book()),
        () -> assertThat(result.reviewTitle()).isEqualTo(review.reviewTitle()),
        () -> assertThat(result.reviewContent()).isEqualTo(review.reviewContent()),
        () -> assertThat(result.pickCount()).isZero());
  }

  @Test
  @DisplayName("서평을 조회한다.")
  void read() {
    // Given
    Review review =
        reviewEntityRepository.create(
            ReviewFixture.create(
                null,
                memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
                bookEntityRepository.create(BookFixture.create(null))));

    // When
    Review result = reviewEntityRepository.read(review.reviewId());

    // Then
    assertThat(result).isEqualTo(review);
  }

  @Test
  @DisplayName("서평을 최신순으로 조회한다.")
  void readAll_SortByLatest_Test() {
    // Given
    Pageable pageable = PageRequest.of(0, 10, Direction.DESC, "reviewId");
    IntStream.range(0, 10)
        .forEach(
            i ->
                reviewEntityRepository.create(
                    ReviewFixture.create(
                        null,
                        memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
                        bookEntityRepository.create(BookFixture.create(null)))));

    // When
    Page<Review> results = reviewEntityRepository.readAll(SortBy.LATEST, pageable);

    // Then
    List<Review> targets =
        new ArrayList<>(List.of(results.getContent().getFirst(), results.getContent().getLast()));
    targets.sort(Comparator.comparing(Review::updatedAt));

    assertThat(targets.getLast().updatedAt()).isAfterOrEqualTo(targets.getFirst().updatedAt());
  }

  @Test
  @DisplayName("서평을 최다픽순으로 조회한다.")
  void readAll_SortByPickCount_Test() {
    // Given
    Pageable pageable = PageRequest.of(0, 10);
    IntStream.range(0, 10)
        .forEach(
            i ->
                reviewEntityRepository.create(
                    ReviewFixture.create(
                        null,
                        memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
                        bookEntityRepository.create(BookFixture.create(null)))));

    // When
    Page<Review> results = reviewEntityRepository.readAll(SortBy.PICK, pageable);

    // Then
    List<Review> content = results.getContent();

    assertThat(content.getFirst().pickCount())
        .isGreaterThanOrEqualTo(content.getLast().pickCount());
  }

  @Test
  @DisplayName("도서에 대한 서평을 조회한다.")
  void readAll_OfBook_Test() {
    // Given
    int count = 10;
    Pageable pageable = PageRequest.of(0, 10);
    Book book = bookEntityRepository.create(BookFixture.create(null));
    IntStream.range(0, count)
        .forEach(
            i ->
                reviewEntityRepository.create(
                    ReviewFixture.create(
                        null,
                        memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
                        book)));

    // When
    Page<Review> results = reviewEntityRepository.readAll(book.bookId(), SortBy.PICK, pageable);

    // Then
    assertThat(results)
        .hasSize(count)
        .allSatisfy(result -> assertThat(result.book()).isEqualTo(book));
  }

  @Test
  @DisplayName("멤버의 서평을 조회한다.")
  void readAll_OfMember_Test() {
    // Given
    Pageable pageable = PageRequest.of(0, 3);
    Member member = memberEntityRepository.create(MemberFixture.create(null, RoleType.USER));
    IntStream.range(0, 5)
        .forEach(
            i ->
                reviewEntityRepository.create(
                    ReviewFixture.create(
                        null, member, bookEntityRepository.create(BookFixture.create(null)))));

    // When
    Page<Review> results = reviewEntityRepository.readAll(member.memberId(), pageable);

    // Then
    assertThat(results)
        .hasSize(pageable.getPageSize())
        .allSatisfy(result -> assertThat(result.member()).isEqualTo(member));
  }

  @Test
  @DisplayName("서평을 수정한다.")
  void update() {
    // Given
    Review review =
        reviewEntityRepository.create(
            ReviewFixture.create(
                null,
                memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
                bookEntityRepository.create(BookFixture.create(null))));
    Review updatingReview = ReviewFixture.create(review.reviewId(), review.member(), review.book());

    // When
    Review result = reviewEntityRepository.update(updatingReview);

    // Then
    assertAll(
        () -> assertThat(result.reviewId()).isEqualTo(updatingReview.reviewId()),
        () -> assertThat(result.member()).isEqualTo(updatingReview.member()),
        () -> assertThat(result.book()).isEqualTo(updatingReview.book()),
        () -> assertThat(result.reviewTitle()).isEqualTo(updatingReview.reviewTitle()),
        () -> assertThat(result.reviewContent()).isEqualTo(updatingReview.reviewContent()),
        () -> assertThat(result.pickCount()).isEqualTo(updatingReview.pickCount()));
  }

  @Test
  @DisplayName("서평을 삭제한다.")
  void delete() {
    // Given
    Review review =
        reviewEntityRepository.create(
            ReviewFixture.create(
                null,
                memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
                bookEntityRepository.create(BookFixture.create(null))));

    // When
    reviewEntityRepository.delete(review.reviewId());

    // Then
    assertThatThrownBy(() -> reviewEntityRepository.read(review.reviewId()))
        .isInstanceOf(JpaObjectRetrievalFailureException.class);
  }
}
