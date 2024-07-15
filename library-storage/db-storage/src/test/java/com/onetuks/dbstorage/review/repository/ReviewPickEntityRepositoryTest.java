package com.onetuks.dbstorage.review.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.dbstorage.DbStorageIntegrationTest;
import com.onetuks.librarydomain.BookFixture;
import com.onetuks.librarydomain.MemberFixture;
import com.onetuks.librarydomain.ReviewFixture;
import com.onetuks.librarydomain.ReviewPickFixture;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.review.model.ReviewPick;
import com.onetuks.libraryobject.enums.RoleType;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

class ReviewPickEntityRepositoryTest extends DbStorageIntegrationTest {

  @Test
  @DisplayName("서평픽을 등록한다.")
  void create() {
    // Given
    ReviewPick reviewPick =
        ReviewPickFixture.create(
            null,
            memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
            reviewEntityRepository.create(
                ReviewFixture.create(
                    null,
                    memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
                    bookEntityRepository.create(BookFixture.create(null)))));

    // When
    ReviewPick result = reviewPickEntityRepository.create(reviewPick);

    // Then
    assertAll(
        () -> assertThat(result.reviewPickId()).isPositive(),
        () -> assertThat(result.member().memberId()).isEqualTo(reviewPick.member().memberId()),
        () -> assertThat(result.review().reviewId()).isEqualTo(reviewPick.review().reviewId()));
  }

  @Test
  @DisplayName("중복된 서평픽 등록 시 예외를 던진다.")
  void create_Duplicated_ExceptionThrown() {
    // Given
    ReviewPick originReviewPick =
        reviewPickEntityRepository.create(
            ReviewPickFixture.create(
                null,
                memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
                reviewEntityRepository.create(
                    ReviewFixture.create(
                        null,
                        memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
                        bookEntityRepository.create(BookFixture.create(null))))));
    ReviewPick reviewPick =
        new ReviewPick(null, originReviewPick.member(), originReviewPick.review());

    // When
    assertThatThrownBy(() -> reviewPickEntityRepository.create(reviewPick))
        .isInstanceOf(DataIntegrityViolationException.class);
  }

  @Test
  @DisplayName("서평픽을 조회한다.")
  void read() {
    // Given
    ReviewPick reviewPick =
        reviewPickEntityRepository.create(
            ReviewPickFixture.create(
                null,
                memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
                reviewEntityRepository.create(
                    ReviewFixture.create(
                        null,
                        memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
                        bookEntityRepository.create(BookFixture.create(null))))));

    // When
    ReviewPick result = reviewPickEntityRepository.read(reviewPick.reviewPickId());

    // Then
    assertAll(
        () -> assertThat(result.reviewPickId()).isPositive(),
        () -> assertThat(result.member().memberId()).isEqualTo(reviewPick.member().memberId()),
        () -> assertThat(result.review().reviewId()).isEqualTo(reviewPick.review().reviewId()));
  }

  @Test
  @DisplayName("해당 멤버의 모든 서평픽을 조회한다.")
  void readAll() {
    // Given
    Pageable pageable = PageRequest.of(0, 10);
    Member picker = memberEntityRepository.create(MemberFixture.create(null, RoleType.USER));
    IntStream.range(0, 15)
        .forEach(
            i ->
                reviewPickEntityRepository.create(
                    ReviewPickFixture.create(
                        null,
                        picker,
                        reviewEntityRepository.create(
                            ReviewFixture.create(
                                null,
                                memberEntityRepository.create(
                                    MemberFixture.create(null, RoleType.USER)),
                                bookEntityRepository.create(BookFixture.create(null)))))));

    // When
    Page<ReviewPick> results = reviewPickEntityRepository.readAll(picker.memberId(), pageable);

    // Then
    assertThat(results)
        .hasSize(pageable.getPageSize())
        .allSatisfy(result -> assertThat(result.member().memberId()).isEqualTo(picker.memberId()));
  }

  @Test
  @DisplayName("서평픽 등록 여부를 조회한다.")
  void read_Existence_test() {
    // Given
    ReviewPick reviewPick =
        reviewPickEntityRepository.create(
            ReviewPickFixture.create(
                null,
                memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
                reviewEntityRepository.create(
                    ReviewFixture.create(
                        null,
                        memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
                        bookEntityRepository.create(BookFixture.create(null))))));

    // When
    boolean result =
        reviewPickEntityRepository.read(
            reviewPick.member().memberId(), reviewPick.review().reviewId());

    // Then
    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("서평픽을 삭제한다.")
  void delete() {
    // Given
    ReviewPick reviewPick =
        reviewPickEntityRepository.create(
            ReviewPickFixture.create(
                null,
                memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
                reviewEntityRepository.create(
                    ReviewFixture.create(
                        null,
                        memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
                        bookEntityRepository.create(BookFixture.create(null))))));

    // When
    reviewPickEntityRepository.delete(reviewPick.reviewPickId());

    // Then
    assertThatThrownBy(() -> reviewPickEntityRepository.read(reviewPick.reviewPickId()))
        .isInstanceOf(JpaObjectRetrievalFailureException.class);
  }
}
