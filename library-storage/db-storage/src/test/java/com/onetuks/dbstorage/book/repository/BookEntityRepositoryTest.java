package com.onetuks.dbstorage.book.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.dbstorage.DbStorageIntegrationTest;
import com.onetuks.librarydomain.BookFixture;
import com.onetuks.librarydomain.book.model.Book;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

class BookEntityRepositoryTest extends DbStorageIntegrationTest {

  @Test
  @DisplayName("도서 엔티티를 생성한다.")
  void create() {
    // Given
    Book book = BookFixture.create(null);

    // When
    Book result = bookEntityRepository.create(book);

    // Then
    assertThat(result.bookId()).isPositive();
  }

  @Test
  @DisplayName("도서 엔티티를 조회한다.")
  void read() {
    // Given
    Book book = bookEntityRepository.create(BookFixture.create(null));

    // When
    Book result = bookEntityRepository.read(book.bookId());

    // Then
    assertThat(result.bookId()).isPositive();
  }

  @Test
  @DisplayName("inspectionMode 가 false 일때 모든 도서를 조회한다.")
  void readAll_InspectionModeFalse_FindAllBooksTest() {
    // Given
    boolean inspectionMode = false;
    Pageable pageable = PageRequest.of(0, 10);
    List<Book> books =
        IntStream.range(0, 10)
            .mapToObj(i -> bookEntityRepository.create(BookFixture.create(null)))
            .toList();

    // When
    Page<Book> results = bookEntityRepository.readAll(inspectionMode, pageable);

    // Then
    assertThat(results.getTotalElements()).isEqualTo(books.size());
  }

  @Test
  @DisplayName("inspectionMode가 true일때 미허가 도서만 조회한다.")
  void readAll_InspectionModeTrue_FindAllNotPermittedBooksTest() {
    // Given
    boolean inspectionMode = true;
    Pageable pageable = PageRequest.of(0, 10);
    List<Book> books =
        IntStream.range(0, 10)
            .mapToObj(i -> bookEntityRepository.create(BookFixture.create(null)))
            .toList();
    List<Book> permittedBooks =
        IntStream.range(0, 3)
            .mapToObj(
                i -> {
                  Book book = books.get(i);
                  return bookEntityRepository.update(
                      book.changeBookInfo(
                          book.title(),
                          book.authorName(),
                          book.introduction(),
                          book.isbn(),
                          book.publisher(),
                          book.categories(),
                          book.isIndie(),
                          true,
                          book.coverImageFile().file()));
                })
            .toList();

    // When
    Page<Book> results = bookEntityRepository.readAll(inspectionMode, pageable);

    // Then
    assertThat(results.getTotalElements()).isEqualTo(books.size() - permittedBooks.size());
    assertThat(results).allSatisfy(result -> assertThat(result.isPermitted()).isFalse());
  }

  @Test
  @DisplayName("도서 엔티티를 수정한다.")
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
  @DisplayName("도서 엔티티를 제거한다.")
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
