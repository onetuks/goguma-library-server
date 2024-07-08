package com.onetuks.dbstorage.book.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.dbstorage.DbStorageIntegrationTest;
import com.onetuks.librarydomain.BookFixture;
import com.onetuks.librarydomain.BookPickFixture;
import com.onetuks.librarydomain.MemberFixture;
import com.onetuks.librarydomain.book.model.BookPick;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.libraryobject.enums.RoleType;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class BookPickEntityRepositoryTest extends DbStorageIntegrationTest {

  @Test
  @DisplayName("북픽을 등록한다.")
  void create() {
    // Given
    BookPick bookPick =
        BookPickFixture.create(
            null,
            memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
            bookEntityRepository.create(BookFixture.create(null)));

    // When
    BookPick result = bookPickEntityRepository.create(bookPick);

    // Then
    assertAll(
        () -> assertThat(result.bookPickId()).isPositive(),
        () -> assertThat(result.member()).isEqualTo(bookPick.member()),
        () -> assertThat(result.book()).isEqualTo(bookPick.book()));
  }

  @Test
  @DisplayName("멤버가 도서를 북픽했는지 여부를 조회한다.")
  void read() {
    // Given
    BookPick bookPick =
        bookPickEntityRepository.create(
            BookPickFixture.create(
                null,
                memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
                bookEntityRepository.create(BookFixture.create(null))));

    // When
    boolean result =
        bookPickEntityRepository.read(bookPick.member().memberId(), bookPick.book().bookId());

    // Then
    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("멤버의 북픽 도서 목록을 조회한다.")
  void readAll() {
    // Given
    Pageable pageable = PageRequest.of(0, 3);
    Member member = memberEntityRepository.create(MemberFixture.create(null, RoleType.USER));
    IntStream.range(0, 5)
        .forEach(
            i ->
                bookPickEntityRepository.create(
                    BookPickFixture.create(
                        null, member, bookEntityRepository.create(BookFixture.create(null)))));

    // When
    Page<BookPick> results = bookPickEntityRepository.readAll(member.memberId(), pageable);

    // Then
    assertThat(results)
        .hasSize(pageable.getPageSize())
        .allSatisfy(result -> assertThat(result.member()).isEqualTo(member));
  }

  @Test
  @DisplayName("북픽을 삭제한다.")
  void delete() {
    // Given
    BookPick bookPick =
        bookPickEntityRepository.create(
            BookPickFixture.create(
                null,
                memberEntityRepository.create(MemberFixture.create(null, RoleType.USER)),
                bookEntityRepository.create(BookFixture.create(null))));

    // When
    bookPickEntityRepository.delete(bookPick.bookPickId());

    // Then
    boolean result =
        bookPickEntityRepository.read(bookPick.member().memberId(), bookPick.book().bookId());

    assertThat(result).isFalse();
  }
}
