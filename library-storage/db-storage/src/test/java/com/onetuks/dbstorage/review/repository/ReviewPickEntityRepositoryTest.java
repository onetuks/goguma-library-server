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
import com.onetuks.libraryobject.exception.NoSuchEntityException;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class ReviewPickEntityRepositoryTest extends DbStorageIntegrationTest {

  @Test
  @DisplayName("서평픽을 등록한다.")
  void create() {
    // Given
    Member member = memberEntityRepository.create(MemberFixture.create(null, RoleType.USER));
    ReviewPick reviewPick =
        ReviewPickFixture.create(
            null,
            memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
            reviewEntityRepository.create(
                ReviewFixture.create(
                    null, member, bookEntityRepository.create(BookFixture.create(null, member)))));

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
    Member member = memberEntityRepository.create(MemberFixture.create(null, RoleType.USER));
    ReviewPick originReviewPick =
        reviewPickEntityRepository.create(
            ReviewPickFixture.create(
                null,
                memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
                reviewEntityRepository.create(
                    ReviewFixture.create(
                        null,
                        member,
                        bookEntityRepository.create(BookFixture.create(null, member))))));
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
    Member member = memberEntityRepository.create(MemberFixture.create(null, RoleType.USER));
    ReviewPick reviewPick =
        reviewPickEntityRepository.create(
            ReviewPickFixture.create(
                null,
                memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
                reviewEntityRepository.create(
                    ReviewFixture.create(
                        null,
                        member,
                        bookEntityRepository.create(BookFixture.create(null, member))))));

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
            i -> {
              Member member =
                  memberEntityRepository.create(MemberFixture.create(null, RoleType.USER));
              reviewPickEntityRepository.create(
                  ReviewPickFixture.create(
                      null,
                      picker,
                      reviewEntityRepository.create(
                          ReviewFixture.create(
                              null,
                              member,
                              bookEntityRepository.create(BookFixture.create(null, member))))));
            });

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
    Member reviewer = memberEntityRepository.create(MemberFixture.create(null, RoleType.USER));
    ReviewPick reviewPick =
        reviewPickEntityRepository.create(
            ReviewPickFixture.create(
                null,
                memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
                reviewEntityRepository.create(
                    ReviewFixture.create(
                        null,
                        reviewer,
                        bookEntityRepository.create(BookFixture.create(null, reviewer))))));

    // When
    ReviewPick result =
        reviewPickEntityRepository.read(
            reviewPick.member().memberId(), reviewPick.review().reviewId());

    // Then
    assertAll(
        () -> assertThat(result.reviewPickId()).isNotNull(),
        () -> assertThat(result.member().memberId()).isEqualTo(reviewPick.member().memberId()),
        () -> assertThat(result.review().reviewId()).isEqualTo(reviewPick.review().reviewId()));
  }

  @Test
  @DisplayName("서평픽을 삭제한다.")
  void delete() {
    // Given
    Member member = memberEntityRepository.create(MemberFixture.create(null, RoleType.USER));
    ReviewPick reviewPick =
        reviewPickEntityRepository.create(
            ReviewPickFixture.create(
                null,
                memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
                reviewEntityRepository.create(
                    ReviewFixture.create(
                        null,
                        member,
                        bookEntityRepository.create(BookFixture.create(null, member))))));

    // When
    reviewPickEntityRepository.delete(reviewPick.reviewPickId());

    // Then
    assertThatThrownBy(() -> reviewPickEntityRepository.read(reviewPick.reviewPickId()))
        .isInstanceOf(NoSuchEntityException.class);
  }
}
