package com.onetuks.librarydomain.book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.onetuks.librarydomain.BookFixture;
import com.onetuks.librarydomain.DomainIntegrationTest;
import com.onetuks.librarydomain.book.model.Book;
import com.onetuks.librarydomain.book.service.dto.param.BookPostParam;
import com.onetuks.libraryobject.enums.ImageType;
import com.onetuks.libraryobject.vo.ImageFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BookServiceTest extends DomainIntegrationTest {

  @Test
  @DisplayName("표지 이미지와 함께 도서 등록한다.")
  void register_WithCoverImage_Test() {
    // Given
    Book book = BookFixture.create(123L);
    BookPostParam param =
        new BookPostParam(
            book.title(),
            book.authorName(),
            book.isbn(),
            book.publisher(),
            book.categories(),
            book.isIndie());

    given(bookRepository.create(any(Book.class))).willReturn(book);

    // When
    Book result = bookService.register(param, book.coverImageFile().file());

    // Then
    assertAll(
        () -> assertThat(result.bookId()).isEqualTo(book.bookId()),
        () -> assertThat(result.title()).isEqualTo(book.title()),
        () -> assertThat(result.authorName()).isEqualTo(book.authorName()),
        () -> assertThat(result.introduction()).isEqualTo(book.introduction()),
        () ->
            assertThat(result.categories()).containsExactlyInAnyOrderElementsOf(book.categories()),
        () -> assertThat(result.isIndie()).isEqualTo(book.isIndie()),
        () -> assertThat(result.isPermitted()).isFalse(),
        () -> assertThat(result.coverImageFile()).isEqualTo(book.coverImageFile()));
  }

  @Test
  @DisplayName("표지 이미지 없이 도서 등록하면 기본 표지 이미지로 할당되어 등록된다.")
  void register_WithOutCoverImage_Test() {
    // Given
    Book book = BookFixture.create(123L);
    BookPostParam param =
        new BookPostParam(
            book.title(),
            book.authorName(),
            book.isbn(),
            book.publisher(),
            book.categories(),
            book.isIndie());
    Book expected =
        Book.of(
            param.title(),
            param.authorName(),
            param.isbn(),
            param.publisher(),
            param.categories(),
            param.isIndie(),
            null);

    given(bookRepository.create(any(Book.class))).willReturn(expected);

    // When
    Book result = bookService.register(param, null);

    // Then
    assertAll(
        () -> assertThat(result.title()).isEqualTo(book.title()),
        () -> assertThat(result.authorName()).isEqualTo(book.authorName()),
        () -> assertThat(result.introduction()).isNull(),
        () ->
            assertThat(result.categories()).containsExactlyInAnyOrderElementsOf(book.categories()),
        () -> assertThat(result.isIndie()).isEqualTo(book.isIndie()),
        () -> assertThat(result.isPermitted()).isFalse(),
        () ->
            assertThat(result.coverImageFile())
                .isEqualTo(ImageFile.of(ImageType.COVER_IMAGE, ImageFile.DEFAULT_COVER_IMAGE_URI)));
  }
}
