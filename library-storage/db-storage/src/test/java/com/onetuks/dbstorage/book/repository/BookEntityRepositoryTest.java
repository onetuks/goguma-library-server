package com.onetuks.dbstorage.book.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.dbstorage.DbStorageIntegrationTest;
import com.onetuks.librarydomain.BookFixture;
import com.onetuks.librarydomain.MemberFixture;
import com.onetuks.librarydomain.book.model.Book;
import com.onetuks.libraryobject.enums.Category;
import com.onetuks.libraryobject.enums.RoleType;
import java.util.List;
import java.util.Set;
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
  @DisplayName("inspectionMode 가 true 일때 미허가 도서만 조회한다.")
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
  @DisplayName("키워드가 주어진 경우, 키워드를 포함하는 제목|저자|출판사를 가진 데이터를 모두 조회한다.")
  void readAll_WithKeyword_FindContainsTest() {
    // Given
    List<Book> books =
        IntStream.range(0, 5)
            .mapToObj(i -> bookEntityRepository.create(BookFixture.create(null)))
            .toList();
    String keyword = books.getFirst().title();
    Pageable pageable = PageRequest.of(0, 10);

    // When
    Page<Book> results = bookEntityRepository.readAll(keyword, pageable);

    // Then
    List<String> tokens = List.of(keyword.split(" "));

    assertThat(results.getContent())
        .allSatisfy(
            result -> {
              boolean contains =
                  tokens.stream()
                      .anyMatch(
                          token ->
                              result.title().contains(token)
                                  || result.authorName().contains(token)
                                  || result.publisher().contains(token));

              assertThat(contains).isTrue();
            });
  }

  @Test
  @DisplayName("키워드 없이 검색하는 경우 모든 도서 데이터를 검색한다.")
  void readAll_WithOutKeyword_FindAllTest() {
    // Given
    Pageable pageable = PageRequest.of(0, 10);
    List<Book> books =
        IntStream.range(0, 5)
            .mapToObj(i -> bookEntityRepository.create(BookFixture.create(null)))
            .toList();

    // When
    Page<Book> results = bookEntityRepository.readAll((String) null, pageable);

    // Then
    assertThat(results.getTotalElements()).isEqualTo(books.size());
  }

  @Test
  @DisplayName("멤버의 관심 카테고리를 포함하는 모든 도서를 조회한다.")
  void readAll_WithInterestedCategories_FindAllTest() {
    // Given
    Pageable pageable = PageRequest.of(0, 10);
    Set<Category> interestedCategories =
        memberEntityRepository
            .create(MemberFixture.create(null, RoleType.USER))
            .interestedCategories();
    List<Book> books =
        IntStream.range(0, 5)
            .mapToObj(
                i -> {
                  Book book = BookFixture.create(null);
                  return bookEntityRepository.create(
                      book.changeBookInfo(
                          book.title(),
                          book.authorName(),
                          book.introduction(),
                          book.isbn(),
                          book.publisher(),
                          interestedCategories,
                          book.isIndie(),
                          book.isPermitted(),
                          book.coverImageFile().file()));
                })
            .toList();

    // When
    Page<Book> results = bookEntityRepository.readAll(interestedCategories, pageable);

    // Then
    assertThat(results)
        .hasSize(books.size())
        .allSatisfy(
            result -> assertThat(result.categories()).containsAnyElementsOf(interestedCategories));
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
