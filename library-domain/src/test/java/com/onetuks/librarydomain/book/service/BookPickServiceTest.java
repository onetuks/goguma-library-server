package com.onetuks.librarydomain.book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.BDDMockito.given;

import com.onetuks.librarydomain.BookFixture;
import com.onetuks.librarydomain.BookPickFixture;
import com.onetuks.librarydomain.DomainIntegrationTest;
import com.onetuks.librarydomain.MemberFixture;
import com.onetuks.librarydomain.book.model.BookPick;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.libraryobject.enums.RoleType;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class BookPickServiceTest extends DomainIntegrationTest {

  @Test
  @DisplayName("북픽을 등록한다.")
  void registerTest() {
    // Given
    BookPick bookPick =
        BookPickFixture.create(
            102L, MemberFixture.create(102L, RoleType.USER), BookFixture.create(102L));

    given(memberRepository.read(bookPick.member().memberId())).willReturn(bookPick.member());
    given(bookRepository.read(bookPick.book().bookId())).willReturn(bookPick.book());
    given(bookPickRepository.create(any(BookPick.class))).willReturn(bookPick);

    // When
    BookPick result =
        bookPickService.register(bookPick.member().memberId(), bookPick.book().bookId());

    // Then
    assertAll(
        () -> assertThat(result.bookPickId()).isPositive(),
        () -> assertThat(result.member()).isEqualTo(bookPick.member()),
        () -> assertThat(result.book()).isEqualTo(bookPick.book()));
  }

  @Test
  @DisplayName("북픽을 삭제한다.")
  void removeTest() {
    // Given
    BookPick bookPick =
        BookPickFixture.create(
            103L, MemberFixture.create(103L, RoleType.USER), BookFixture.create(103L));

    given(bookPickRepository.read(bookPick.bookPickId())).willReturn(bookPick);

    // When
    bookPickService.remove(bookPick.member().memberId(), bookPick.bookPickId());

    // Then
    verify(bookPickRepository, times(1)).delete(bookPick.bookPickId());
  }

  @Test
  @DisplayName("해당 멤버의 모든 북픽을 조회한다.")
  void searchExistenceAll() {
    // Given
    Pageable pageable = PageRequest.of(0, 3);
    Member member = MemberFixture.create(100L, RoleType.USER);
    Page<BookPick> bookPicks =
        new PageImpl<>(
            IntStream.range(0, 5)
                .mapToObj(
                    i -> BookPickFixture.create((long) i, member, BookFixture.create((long) i)))
                .toList());
    Page<BookPick> expected =
        new PageImpl<>(
            bookPicks.getContent().subList(0, pageable.getPageSize()),
            pageable,
            bookPicks.getTotalElements());

    given(bookPickRepository.readAll(member.memberId(), pageable)).willReturn(expected);

    // When
    Page<BookPick> results = bookPickService.searchAll(member.memberId(), pageable);

    // Then
    assertThat(results).hasSize(pageable.getPageSize());
  }

  @Test
  @DisplayName("해당 멤버가 북픽한 도서라면 true를 반환한다.")
  void searchExistenceTest() {
    // Given
    BookPick bookPick =
        BookPickFixture.create(
            101L, MemberFixture.create(101L, RoleType.USER), BookFixture.create(101L));

    given(bookPickRepository.read(bookPick.member().memberId(), bookPick.book().bookId()))
        .willReturn(true);

    // When
    boolean result =
        bookPickService.searchExistence(bookPick.member().memberId(), bookPick.book().bookId());

    // Then
    assertThat(result).isTrue();
  }
}
