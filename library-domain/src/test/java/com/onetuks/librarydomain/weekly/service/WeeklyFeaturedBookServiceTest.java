package com.onetuks.librarydomain.weekly.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;

import com.onetuks.librarydomain.BookFixture;
import com.onetuks.librarydomain.DomainIntegrationTest;
import com.onetuks.librarydomain.WeeklyFeaturedBookFixture;
import com.onetuks.librarydomain.book.model.Book;
import com.onetuks.librarydomain.weekly.model.WeeklyFeaturedBook;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class WeeklyFeaturedBookServiceTest extends DomainIntegrationTest {

  @Test
  @DisplayName("이번주의 금주도서를 등록하면 기존 금주도서였던 적이 없는 도서로 등록한다.")
  void registerAll() {
    // Given
    Pageable pageable = PageRequest.of(0, 3);
    List<Book> allBooks =
        IntStream.range(0, 10).mapToObj(i -> BookFixture.create((long) i)).toList();
    List<WeeklyFeaturedBook> allWeeklyFeaturedBooks =
        IntStream.range(0, 5)
            .mapToObj(i -> WeeklyFeaturedBookFixture.create(101L, allBooks.get(i)))
            .toList();
    List<Book> featuredBooks =
        allBooks.subList(allWeeklyFeaturedBooks.size(), allWeeklyFeaturedBooks.size() + 3);

    given(weeklyFeaturedBookRepository.readAll()).willReturn(allWeeklyFeaturedBooks);
    given(bookRepository.readAllNotIn(anyList())).willReturn(featuredBooks);

    // When
    weeklyFeaturedBookService.registerAll();

    // Then
    verify(weeklyFeaturedBookRepository, times(pageable.getPageSize()))
        .create(any(WeeklyFeaturedBook.class));
  }

  @Test
  @DisplayName("이번주의 금주도서를 조회하면 3권의 도서가 조회된다.")
  void searchAllForThisWeek() {
    // Given
    int count = 3;
    Page<WeeklyFeaturedBook> weeklyFeaturedBooks =
        new PageImpl<>(
            IntStream.range(0, count)
                .mapToObj(
                    i -> WeeklyFeaturedBookFixture.create((long) i, BookFixture.create((long) i)))
                .toList());

    given(weeklyFeaturedBookRepository.readAllForThisWeek()).willReturn(weeklyFeaturedBooks);

    // When
    Page<Book> results = weeklyFeaturedBookService.searchAllForThisWeek();

    // Then
    assertThat(results).hasSize(count);
  }
}
