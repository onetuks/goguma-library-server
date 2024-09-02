package com.onetuks.librarydomain.book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;

import com.onetuks.librarydomain.BookFixture;
import com.onetuks.librarydomain.BookPickFixture;
import com.onetuks.librarydomain.DomainIntegrationTest;
import com.onetuks.librarydomain.MemberFixture;
import com.onetuks.librarydomain.book.model.BookPick;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.libraryobject.enums.RoleType;
import com.onetuks.libraryobject.exception.ApiAccessDeniedException;
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
    Member member = MemberFixture.create(102L, RoleType.USER);
    BookPick bookPick = BookPickFixture.create(102L, member, BookFixture.create(102L, member));

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
    Member member = MemberFixture.create(103L, RoleType.USER);
    BookPick bookPick = BookPickFixture.create(103L, member, BookFixture.create(103L, member));

    given(bookPickRepository.read(bookPick.bookPickId())).willReturn(bookPick);

    // When
    bookPickService.remove(bookPick.member().memberId(), bookPick.bookPickId());

    // Then
    verify(bookPickRepository, times(1)).delete(bookPick.bookPickId());
  }

  @Test
  @DisplayName("권한 없는 멤버가 북픽 삭제 시 예외를 던진다.")
  void remove_NotAuthMember_Exception() {
    // Given
    long notAuthMemberId = 1L;
    Member member = MemberFixture.create(103L, RoleType.USER);
    BookPick bookPick = BookPickFixture.create(103L, member, BookFixture.create(103L, member));

    given(bookPickRepository.read(bookPick.bookPickId())).willReturn(bookPick);

    // When
    assertThatThrownBy(() -> bookPickService.remove(notAuthMemberId, bookPick.bookPickId()))
        .isInstanceOf(ApiAccessDeniedException.class);

    // Then
    verify(bookPickRepository, times(0)).delete(bookPick.bookPickId());
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
                    i ->
                        BookPickFixture.create(
                            (long) i, member, BookFixture.create((long) i, member)))
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
  @DisplayName("해당 멤버가 북픽한 도서라면 해당 북픽 정보를 반환한다.")
  void searchExistenceTest() {
    // Given
    Member picker = MemberFixture.create(101L, RoleType.USER);
    BookPick bookPick = BookPickFixture.create(101L, picker, BookFixture.create(101L, picker));

    given(bookPickRepository.read(picker.memberId(), bookPick.book().bookId()))
        .willReturn(bookPick);

    // When
    BookPick result = bookPickService.searchExistence(picker.memberId(), bookPick.book().bookId());

    // Then
    assertAll(
        () -> assertThat(result.bookPickId()).isNotNull(),
        () -> assertThat(result.member().memberId()).isEqualTo(picker.memberId()),
        () -> assertThat(result.book().bookId()).isEqualTo(bookPick.book().bookId()));
  }
}
