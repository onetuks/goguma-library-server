package com.onetuks.dbstorage.weekly.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.dbstorage.DbStorageIntegrationTest;
import com.onetuks.librarydomain.BookFixture;
import com.onetuks.librarydomain.WeeklyFeaturedBookFixture;
import com.onetuks.librarydomain.book.model.Book;
import com.onetuks.librarydomain.weekly.model.WeeklyFeaturedBook;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

class WeeklyFeaturedBookEntityRepositoryTest extends DbStorageIntegrationTest {

  @Test
  @DisplayName("금주도서를 등록한다.")
  void create_Test() {
    // Given
    Book featuredBook = bookEntityRepository.create(BookFixture.create(null));
    WeeklyFeaturedBook weeklyFeaturedBook = WeeklyFeaturedBookFixture.create(null, featuredBook);

    // When
    WeeklyFeaturedBook result = weeklyFeaturedBookEntityRepository.create(weeklyFeaturedBook);

    // Then
    assertAll(
        () -> assertThat(result.weeklyFeaturedBookId()).isNotNull(),
        () ->
            assertThat(result.weeklyFeaturedBooksEvent().weeklyFeaturedBooksEventId()).isNotNull(),
        () ->
            assertThat(result.weeklyFeaturedBooksEvent().startedAt())
                .isBeforeOrEqualTo(LocalDateTime.now().toLocalDate().atStartOfDay()),
        () ->
            assertThat(result.weeklyFeaturedBooksEvent().endedAt())
                .isAfterOrEqualTo(LocalDateTime.now()),
        () -> assertThat(result.book().bookId()).isEqualTo(featuredBook.bookId()));
  }

  @Test
  @DisplayName("금주도서 중 실제 이번주 금주도서만 다건 조회한다.")
  void readAll_ForThisWeek_OnlyThisWeek_Test() {
    // Given
    int count = 3;
    IntStream.range(0, count)
        .forEach(
            i ->
                weeklyFeaturedBookEntityRepository.create(
                    WeeklyFeaturedBook.of(bookEntityRepository.create(BookFixture.create(null)))));

    // When
    Page<WeeklyFeaturedBook> results = weeklyFeaturedBookEntityRepository.readAllForThisWeek();

    // Then
    assertThat(results.getContent())
        .hasSize(count)
        .allSatisfy(
            result -> {
              assertThat(result.weeklyFeaturedBooksEvent().startedAt())
                  .isBeforeOrEqualTo(LocalDateTime.now());
              assertThat(result.weeklyFeaturedBooksEvent().endedAt())
                  .isAfterOrEqualTo(LocalDateTime.now());
            });
  }

  @Test
  @DisplayName("모든 금주도서를 다건 조회한다.")
  void readAll_ForThisWeek_Test() {
    // Given
    int count = 10;
    IntStream.range(0, count)
        .mapToObj(i -> bookEntityRepository.create(BookFixture.create(null)))
        .forEach(
            featuredBook ->
                weeklyFeaturedBookEntityRepository.create(WeeklyFeaturedBook.of(featuredBook)));

    // When
    List<WeeklyFeaturedBook> results = weeklyFeaturedBookEntityRepository.readAll();

    // Then
    assertThat(results)
        .hasSize(count)
        .allSatisfy(
            result -> {
              assertThat(result.weeklyFeaturedBookId()).isNotNull();
              assertThat(result.weeklyFeaturedBooksEvent().startedAt())
                  .isBeforeOrEqualTo(LocalDateTime.now());
              assertThat(result.weeklyFeaturedBooksEvent().endedAt())
                  .isAfterOrEqualTo(LocalDateTime.now());
            });
  }
}
