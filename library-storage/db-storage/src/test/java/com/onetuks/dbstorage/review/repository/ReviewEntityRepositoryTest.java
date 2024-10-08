package com.onetuks.dbstorage.review.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.dbstorage.DbStorageIntegrationTest;
import com.onetuks.librarydomain.BookFixture;
import com.onetuks.librarydomain.MemberFixture;
import com.onetuks.librarydomain.ReviewFixture;
import com.onetuks.librarydomain.ReviewPickFixture;
import com.onetuks.librarydomain.book.model.Book;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.review.model.Review;
import com.onetuks.libraryobject.enums.RoleType;
import com.onetuks.libraryobject.enums.SortBy;
import com.onetuks.libraryobject.exception.NoSuchEntityException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

class ReviewEntityRepositoryTest extends DbStorageIntegrationTest {

  @Test
  @DisplayName("서평을 등록한다.")
  void create() {
    // Given
    Member member = memberEntityRepository.create(MemberFixture.create(null, RoleType.USER));
    Review review =
        ReviewFixture.create(
            null, member, bookEntityRepository.create(BookFixture.create(null, member)));

    // When
    Review result = reviewEntityRepository.create(review);

    // Then
    assertAll(
        () -> assertThat(result.reviewId()).isPositive(),
        () -> assertThat(result.member()).isEqualTo(review.member()),
        () -> assertThat(result.book().bookId()).isEqualTo(review.book().bookId()),
        () -> assertThat(result.reviewTitle()).isEqualTo(review.reviewTitle()),
        () -> assertThat(result.reviewContent()).isEqualTo(review.reviewContent()),
        () -> assertThat(result.pickCount()).isZero());
  }

  @Test
  @DisplayName("서평을 조회한다.")
  void read() {
    // Given
    Member member = memberEntityRepository.create(MemberFixture.create(null, RoleType.USER));
    Review review =
        reviewEntityRepository.create(
            ReviewFixture.create(
                null, member, bookEntityRepository.create(BookFixture.create(null, member))));

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
            i -> {
              Member member =
                  memberEntityRepository.create(MemberFixture.create(null, RoleType.USER));
              reviewEntityRepository.create(
                  ReviewFixture.create(
                      null, member, bookEntityRepository.create(BookFixture.create(null, member))));
            });

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
            i -> {
              Member member =
                  memberEntityRepository.create(MemberFixture.create(null, RoleType.USER));
              Review review =
                  reviewEntityRepository.create(
                      ReviewFixture.create(
                          null,
                          member,
                          bookEntityRepository.create(BookFixture.create(null, member))));

              IntStream.range(0, i * 2)
                  .forEach(
                      j ->
                          reviewPickEntityRepository.create(
                              ReviewPickFixture.create(
                                  null,
                                  memberEntityRepository.create(
                                      MemberFixture.create(null, RoleType.USER)),
                                  review)));
            });

    // When
    Page<Review> results = reviewEntityRepository.readAll(SortBy.PICK, pageable);

    // Then
    List<Review> content = results.getContent();

    assertThat(content.getFirst().pickCount())
        .isGreaterThanOrEqualTo(content.getLast().pickCount());
  }

  @Test
  @DisplayName("도서에 대한 서평을 최신순으로 조회한다.")
  void readAll_OfBookOrderByLatest_Test() {
    // Given
    int count = 10;
    Pageable pageable = PageRequest.of(0, 10);
    Member member = memberEntityRepository.create(MemberFixture.create(null, RoleType.USER));
    Book book = bookEntityRepository.create(BookFixture.create(null, member));
    List<Review> reviews =
        IntStream.range(0, count)
            .mapToObj(
                i ->
                    reviewEntityRepository.create(
                        ReviewFixture.create(
                            null,
                            memberEntityRepository.create(
                                MemberFixture.create(null, RoleType.USER)),
                            book)))
            .toList();
    reviews.forEach(
        review ->
            reviewPickEntityRepository.create(
                ReviewPickFixture.create(
                    null,
                    memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
                    review)));

    // When
    Page<Review> results =
        reviewEntityRepository.readAllByBook(book.bookId(), SortBy.LATEST, pageable);

    // Then
    assertThat(results)
        .hasSize(count)
        .allSatisfy(result -> assertThat(result.book().bookId()).isEqualTo(book.bookId()));
  }

  @Test
  @DisplayName("도서에 대한 서평을 서평픽 순으로 조회한다.")
  void readAll_OfBookOrderByPickCount_Test() {
    // Given
    int count = 10;
    Pageable pageable = PageRequest.of(0, 10);
    Member member = memberEntityRepository.create(MemberFixture.create(null, RoleType.USER));
    Book book = bookEntityRepository.create(BookFixture.create(null, member));
    IntStream.range(0, count)
        .forEach(
            i -> {
              Review review =
                  reviewEntityRepository.create(
                      ReviewFixture.create(
                          null,
                          memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
                          book));
              IntStream.range(0, i * 2)
                  .forEach(
                      j ->
                          reviewPickEntityRepository.create(
                              ReviewPickFixture.create(
                                  null,
                                  memberEntityRepository.create(
                                      MemberFixture.create(null, RoleType.USER)),
                                  review)));
            });

    // When
    Page<Review> results =
        reviewEntityRepository.readAllByBook(book.bookId(), SortBy.PICK, pageable);

    // Then
    assertThat(results)
        .hasSize(count)
        .allSatisfy(result -> assertThat(result.book().bookId()).isEqualTo(book.bookId()));
  }

  @Test
  @DisplayName("멤버의 서평을 조회한다.")
  void readAll_OfMember_Test() {
    // Given
    Pageable pageable = PageRequest.of(0, 3);
    SortBy sortBy = SortBy.LATEST;
    Member member = memberEntityRepository.create(MemberFixture.create(null, RoleType.USER));
    IntStream.range(0, 5)
        .forEach(
            i ->
                reviewEntityRepository.create(
                    ReviewFixture.create(
                        null,
                        member,
                        bookEntityRepository.create(BookFixture.create(null, member)))));

    // When
    Page<Review> results =
        reviewEntityRepository.readAllByMember(member.memberId(), sortBy, pageable);

    // Then
    assertThat(results)
        .hasSize(pageable.getPageSize())
        .allSatisfy(result -> assertThat(result.member()).isEqualTo(member));
  }

  @Test
  @DisplayName("관심 카테고리에 해당하는 도서에 대한 서평을 조회한다.")
  void readAll_OfBooks_Test() {
    // Given
    Pageable pageable = PageRequest.of(0, 3);
    Member member = memberEntityRepository.create(MemberFixture.create(null, RoleType.USER));
    List<Book> thisWeekInterestedCategoriesBooks =
        IntStream.range(0, 3)
            .mapToObj(i -> bookEntityRepository.create(BookFixture.create(null, member)))
            .toList();
    thisWeekInterestedCategoriesBooks.forEach(
        book ->
            reviewEntityRepository.create(
                ReviewFixture.create(
                    null,
                    memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
                    book)));

    // When
    Page<Review> results =
        reviewEntityRepository.readAll(thisWeekInterestedCategoriesBooks, pageable);

    // Then
    Set<Long> thisWeekInterestedCategoriesBookIds =
        thisWeekInterestedCategoriesBooks.stream().map(Book::bookId).collect(Collectors.toSet());

    assertThat(results)
        .hasSize(pageable.getPageSize())
        .allSatisfy(
            result -> assertThat(result.book().bookId()).isIn(thisWeekInterestedCategoriesBookIds));
  }

  @Test
  @DisplayName("금주도서에 해당하는 서평 중 서평픽이 많은 순으로 7건 조회한다.")
  void readAllWeeklyMostPicked_OnlyWeeklyFeaturedBookReview_OrderByPickCountDesc_Test() {
    // Given
    int count = 7;
    Member member = memberEntityRepository.create(MemberFixture.create(null, RoleType.USER));
    List<Book> thisWeekFeaturedBooks =
        IntStream.range(0, 5)
            .mapToObj(i -> bookEntityRepository.create(BookFixture.create(null, member)))
            .toList();
    thisWeekFeaturedBooks.forEach(
        book -> {
          List<Review> reviews =
              IntStream.range(0, 3)
                  .mapToObj(
                      i ->
                          reviewEntityRepository.create(
                              ReviewFixture.create(
                                  null,
                                  memberEntityRepository.create(
                                      MemberFixture.create(null, RoleType.USER)),
                                  book)))
                  .toList();

          reviews.forEach(
              review ->
                  reviewPickEntityRepository.create(
                      ReviewPickFixture.create(
                          null,
                          memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
                          review)));
        });

    // When
    Page<Review> results = reviewEntityRepository.readAllWeeklyMostPicked(thisWeekFeaturedBooks);

    // Then
    long firstReviewPickCount = results.getContent().getFirst().pickCount();
    long lastReviewPickCount = results.getContent().getFirst().pickCount();

    assertAll(
        () -> assertThat(results).hasSize(count),
        () -> assertThat(firstReviewPickCount).isGreaterThanOrEqualTo(lastReviewPickCount));
  }

  @Test
  @DisplayName("금주도서에 해당하는 서평을 가장 많이 작성한 순으로 3명 조회한다.")
  void readAllWeeklyMostWrite_OnlyWeeklyFeaturedBookReview_OrderByWriteCount_Test() {
    // Given
    int count = 3;
    SortBy sortBy = SortBy.LATEST;
    Member member = memberEntityRepository.create(MemberFixture.create(null, RoleType.USER));
    List<Book> thisWeekFeaturedBooks =
        IntStream.range(0, 3)
            .mapToObj(i -> bookEntityRepository.create(BookFixture.create(null, member)))
            .toList();
    List<Member> members =
        IntStream.range(0, 4)
            .mapToObj(i -> memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)))
            .toList();
    IntStream.range(0, 3)
        .forEach(
            i ->
                reviewEntityRepository.create(
                    ReviewFixture.create(null, members.getFirst(), thisWeekFeaturedBooks.get(i))));
    IntStream.range(0, 2)
        .forEach(
            i ->
                reviewEntityRepository.create(
                    ReviewFixture.create(null, members.get(1), thisWeekFeaturedBooks.get(i))));
    IntStream.range(0, 1)
        .forEach(
            i ->
                reviewEntityRepository.create(
                    ReviewFixture.create(null, members.get(2), thisWeekFeaturedBooks.get(i))));

    // When
    Page<Member> results = reviewEntityRepository.readAllWeeklyMostWrite(thisWeekFeaturedBooks);

    // Then
    Member firstMember = results.getContent().getFirst();
    Member lastMember = results.getContent().getLast();

    long firstMemberReviewCount =
        reviewEntityRepository
            .readAllByMember(firstMember.memberId(), sortBy, PageRequest.of(0, 1_000))
            .getTotalElements();
    long lastMemberReviewCount =
        reviewEntityRepository
            .readAllByMember(lastMember.memberId(), sortBy, PageRequest.of(0, 1_000))
            .getTotalElements();

    assertAll(
        () -> assertThat(results).hasSize(count),
        () -> assertThat(firstMemberReviewCount).isGreaterThanOrEqualTo(lastMemberReviewCount));
  }

  @Test
  @DisplayName("서평을 수정한다.")
  void update() {
    // Given
    Member member = memberEntityRepository.create(MemberFixture.create(null, RoleType.USER));
    Review review =
        reviewEntityRepository.create(
            ReviewFixture.create(
                null, member, bookEntityRepository.create(BookFixture.create(null, member))));
    Review updatingReview = ReviewFixture.create(review.reviewId(), review.member(), review.book());

    // When
    Review result = reviewEntityRepository.update(updatingReview);

    // Then
    assertAll(
        () -> assertThat(result.reviewId()).isEqualTo(updatingReview.reviewId()),
        () -> assertThat(result.member()).isEqualTo(updatingReview.member()),
        () -> assertThat(result.book().bookId()).isEqualTo(updatingReview.book().bookId()),
        () -> assertThat(result.reviewTitle()).isEqualTo(updatingReview.reviewTitle()),
        () -> assertThat(result.reviewContent()).isEqualTo(updatingReview.reviewContent()),
        () -> assertThat(result.pickCount()).isEqualTo(updatingReview.pickCount()));
  }

  @Test
  @DisplayName("서평을 삭제한다.")
  void delete() {
    // Given
    Member member = memberEntityRepository.create(MemberFixture.create(null, RoleType.USER));
    Review review =
        reviewEntityRepository.create(
            ReviewFixture.create(
                null, member, bookEntityRepository.create(BookFixture.create(null, member))));

    // When
    reviewEntityRepository.delete(review.reviewId());

    // Then
    assertThatThrownBy(() -> reviewEntityRepository.read(review.reviewId()))
        .isInstanceOf(NoSuchEntityException.class);
  }
}
