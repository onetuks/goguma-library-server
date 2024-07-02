package com.onetuks.dbstorage.book.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.dbstorage.DbStorageIntegrationTest;
import com.onetuks.librarydomain.BookFixture;
import com.onetuks.librarydomain.book.model.Book;
import org.junit.jupiter.api.Test;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

class BookEntityRepositoryTest extends DbStorageIntegrationTest {

  @Test
  void create() {
    // Given
    Book book = BookFixture.create(null);

    // When
    Book result = bookEntityRepository.create(book);

    // Then
    assertThat(result.bookId()).isPositive();
  }

  @Test
  void read() {
    // Given
    Book book = bookEntityRepository.create(BookFixture.create(null));

    // When
    Book result = bookEntityRepository.read(book.bookId());

    // Then
    assertThat(result.bookId()).isPositive();
  }

  @Test
  void update() {
    // Given
    Book book = bookEntityRepository.create(BookFixture.create(null));
    Book expected = BookFixture.create(book.bookId());

    // When
    Book result = bookEntityRepository.update(expected);

    // Then
    assertAll(
        () -> assertThat(result.bookId()).isEqualTo(book.bookId()),
        () -> assertThat(result.title()).isEqualTo(expected.title()),
        () -> assertThat(result.authorName()).isEqualTo(expected.authorName()),
        () -> assertThat(result.introduction()).isEqualTo(expected.introduction()),
        () -> assertThat(result.isbn()).isEqualTo(expected.isbn()),
        () -> assertThat(result.publisher()).isEqualTo(expected.publisher()),
        () ->
            assertThat(result.categories())
                .containsExactlyInAnyOrderElementsOf(expected.categories()),
        () -> assertThat(result.coverImageFile()).isEqualTo(expected.coverImageFile()),
        () -> assertThat(result.isIndie()).isEqualTo(expected.isIndie()),
        () -> assertThat(result.isPermitted()).isEqualTo(expected.isPermitted()));
  }

  @Test
  void delete() {
    // Given
    Book book = bookEntityRepository.create(BookFixture.create(null));

    // When
    bookEntityRepository.delete(book.bookId());

    // Then
    assertThatThrownBy(() -> bookEntityRepository.read(book.bookId()))
        .isInstanceOf(JpaObjectRetrievalFailureException.class);
  }
}
