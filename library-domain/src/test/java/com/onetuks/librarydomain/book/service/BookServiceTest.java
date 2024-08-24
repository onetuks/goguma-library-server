package com.onetuks.librarydomain.book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.onetuks.librarydomain.BookFixture;
import com.onetuks.librarydomain.DomainIntegrationTest;
import com.onetuks.librarydomain.MemberFixture;
import com.onetuks.librarydomain.book.model.Book;
import com.onetuks.librarydomain.book.service.dto.param.BookPatchParam;
import com.onetuks.librarydomain.book.service.dto.param.BookPostParam;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.libraryobject.MultipartFileFixture;
import com.onetuks.libraryobject.enums.Category;
import com.onetuks.libraryobject.enums.ImageType;
import com.onetuks.libraryobject.enums.RoleType;
import com.onetuks.libraryobject.vo.ImageFile;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

class BookServiceTest extends DomainIntegrationTest {

  @Test
  @DisplayName("표지 이미지와 함께 도서 등록한다.")
  void register_WithCoverImage_Test() {
    // Given
    long loginId = 123L;
    Book book = BookFixture.create(123L, MemberFixture.create(123L, RoleType.USER));
    BookPostParam param =
        new BookPostParam(
            book.title(),
            book.authorName(),
            book.introduction(),
            book.isbn(),
            book.publisher(),
            book.categories(),
            book.isIndie(),
            book.coverImageFile().fileName());

    given(bookRepository.create(any(Book.class))).willReturn(book);

    // When
    Book result = bookService.register(loginId, param, book.coverImageFile().file());

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

    verify(pointService, times(1)).creditPointForBookRegistration(loginId);
    verify(fileRepository, times(1)).putFile(any());
  }

  @Test
  @DisplayName("표지 이미지 없이 도서 등록하면 기본 표지 이미지로 할당되어 등록된다.")
  void register_WithOutCoverImage_Test() {
    // Given
    long loginId = 123L;
    Book book = BookFixture.create(123L, MemberFixture.create(123L, RoleType.USER));
    BookPostParam param =
        new BookPostParam(
            book.title(),
            book.authorName(),
            book.introduction(),
            book.isbn(),
            book.publisher(),
            book.categories(),
            book.isIndie(),
            book.coverImageFile().fileName());
    Book expected =
        Book.of(
            book.member(),
            param.title(),
            param.authorName(),
            param.introduction(),
            param.isbn(),
            param.publisher(),
            param.categories(),
            param.isIndie(),
            param.coverImageFilename(),
            null);

    given(bookRepository.create(any(Book.class))).willReturn(expected);

    // When
    Book result = bookService.register(loginId, param, null);

    // Then
    assertAll(
        () -> assertThat(result.title()).isEqualTo(book.title()),
        () -> assertThat(result.authorName()).isEqualTo(book.authorName()),
        () -> assertThat(result.introduction()).isEqualTo(book.introduction()),
        () ->
            assertThat(result.categories()).containsExactlyInAnyOrderElementsOf(book.categories()),
        () -> assertThat(result.isIndie()).isEqualTo(book.isIndie()),
        () -> assertThat(result.isPermitted()).isFalse(),
        () ->
            assertThat(result.coverImageFile())
                .isEqualTo(ImageFile.of(ImageType.COVER_IMAGE, ImageFile.DEFAULT_COVER_IMAGE_URI)));

    verify(pointService, times(1)).creditPointForBookRegistration(loginId);
    verify(fileRepository, times(1)).putFile(any());
  }

  @Test
  @DisplayName("커버 이미지 없이 도서 정보를 수정하면 커버 이미지 제외한 정보만 수정된다.")
  void edit_WithOutCoverImage_Test() {
    // Given
    Book book = BookFixture.create(124L, MemberFixture.create(124L, RoleType.USER));
    BookPatchParam param =
        new BookPatchParam(
            "새로운 제목",
            "새로운 작가명",
            "새로운 소개",
            "1110111011101",
            "새로운 출판사",
            Set.of(Category.CARTOON, Category.NOVEL),
            true,
            true,
            book.coverImageFile().fileName());
    Book updatedBook =
        new Book(
            book.bookId(),
            book.member(),
            param.title(),
            param.authorName(),
            param.introduction(),
            param.isbn(),
            param.publisher(),
            param.categories(),
            book.coverImageFile(),
            param.isIndie(),
            param.isPermitted(),
            book.pickCounts(),
            book.createdAt());

    given(bookRepository.read(book.bookId())).willReturn(book);
    given(bookRepository.update(any(Book.class))).willReturn(updatedBook);

    // When
    Book result = bookService.edit(book.bookId(), param, null);

    // Then
    assertAll(
        () -> assertThat(result.title()).isEqualTo(param.title()),
        () -> assertThat(result.authorName()).isEqualTo(param.authorName()),
        () -> assertThat(result.introduction()).isEqualTo(param.introduction()),
        () -> assertThat(result.isbn()).isEqualTo(param.isbn()),
        () -> assertThat(result.publisher()).isEqualTo(param.publisher()),
        () ->
            assertThat(result.categories()).containsExactlyInAnyOrderElementsOf(param.categories()),
        () -> assertThat(result.isIndie()).isEqualTo(param.isIndie()),
        () -> assertThat(result.isPermitted()).isEqualTo(param.isPermitted()),
        () -> assertThat(result.coverImageFile()).isEqualTo(book.coverImageFile()));
  }

  @Test
  @DisplayName("커버 이미지와 함께 수정하면 커버 이미지도 변경된다.")
  void edit_WithCoverImage_Test() {
    // Given
    Book book = BookFixture.create(124L, MemberFixture.create(124L, RoleType.USER));
    BookPatchParam param =
        new BookPatchParam(
            "새로운 제목",
            "새로운 작가명",
            "새로운 소개",
            "1110111011101",
            "새로운 출판사",
            Set.of(Category.CARTOON, Category.NOVEL),
            true,
            true,
            book.coverImageFile().fileName());
    MultipartFile coverImage =
        MultipartFileFixture.create(ImageType.COVER_IMAGE, UUID.randomUUID().toString());
    Book updatedBook =
        new Book(
            book.bookId(),
            book.member(),
            param.title(),
            param.authorName(),
            param.introduction(),
            param.isbn(),
            param.publisher(),
            param.categories(),
            ImageFile.of(
                ImageType.COVER_IMAGE,
                Objects.requireNonNull(coverImage),
                book.coverImageFile().fileName()),
            param.isIndie(),
            param.isPermitted(),
            book.pickCounts(),
            book.createdAt());

    given(bookRepository.read(book.bookId())).willReturn(book);
    given(bookRepository.update(any(Book.class))).willReturn(updatedBook);

    // When
    Book result = bookService.edit(book.bookId(), param, coverImage);

    // Then
    assertAll(
        () -> assertThat(result.title()).isEqualTo(param.title()),
        () -> assertThat(result.authorName()).isEqualTo(param.authorName()),
        () -> assertThat(result.introduction()).isEqualTo(param.introduction()),
        () -> assertThat(result.isbn()).isEqualTo(param.isbn()),
        () -> assertThat(result.publisher()).isEqualTo(param.publisher()),
        () ->
            assertThat(result.categories()).containsExactlyInAnyOrderElementsOf(param.categories()),
        () -> assertThat(result.isIndie()).isEqualTo(param.isIndie()),
        () -> assertThat(result.isPermitted()).isEqualTo(param.isPermitted()),
        () -> assertThat(result.coverImageFile().file()).isEqualTo(coverImage),
        () -> assertThat(result.coverImageFile()).isEqualTo(book.coverImageFile()));
  }

  @Test
  @DisplayName("도서를 삭제하면 커버 이미지도 함께 삭제된다. 기본 이미지면 삭제되지 않는다.")
  void removeTest() {
    // Given
    Book book = BookFixture.create(125L, MemberFixture.create(123L, RoleType.USER));

    given(bookRepository.read(book.bookId())).willReturn(book);

    // When
    bookService.remove(book.bookId());

    // Then
    verify(fileRepository, times(1)).deleteFile(book.coverImageFile());
    verify(bookRepository, times(1)).delete(book.bookId());
  }

  @Test
  @DisplayName("검수 대상인 모든 도서를 조회한다.")
  void findAll_ForInspection_Test() {
    // Given
    boolean inspectionMode = true;
    int counts = 5;
    Pageable pageable = PageRequest.of(0, 10);
    Page<Book> books =
        new PageImpl<>(
            IntStream.range(0, counts)
                .mapToObj(
                    i -> BookFixture.create((long) i, MemberFixture.create(123L, RoleType.USER)))
                .toList());

    given(bookRepository.readAll(inspectionMode, pageable)).willReturn(books);

    // When
    Page<Book> results = bookService.searchForInspection(inspectionMode, pageable);

    // Then
    assertThat(results.getTotalElements()).isEqualTo(counts);
  }

  @Test
  @DisplayName("도서 ID로 도서를 조회한다.")
  void findTest() {
    // Given
    Book book = BookFixture.create(126L, MemberFixture.create(123L, RoleType.USER));

    given(bookRepository.read(book.bookId())).willReturn(book);

    // When
    Book result = bookService.search(book.bookId());

    // Then
    assertAll(
        () -> assertThat(result.isPermitted()).isEqualTo(book.isPermitted()),
        () -> assertThat(result.title()).isEqualTo(book.title()));
  }

  @Test
  @DisplayName("제목/작가명 키워드를 포함하는 도서를 다건 조회한다.")
  void findAll_WithKeyword_Test() {
    // Given
    Page<Book> books =
        new PageImpl<>(
            IntStream.range(0, 5)
                .mapToObj(
                    i -> BookFixture.create((long) i, MemberFixture.create(123L, RoleType.USER)))
                .toList());
    String keyword = books.getContent().getFirst().title();
    Pageable pageable = PageRequest.of(0, 10);
    List<String> keywordTokens = List.of(keyword.split(" "));
    Page<Book> expected =
        new PageImpl<>(
            books.stream()
                .filter(
                    book ->
                        keywordTokens.stream()
                            .anyMatch(
                                token ->
                                    book.title().contains(token)
                                        || book.authorName().contains(token)
                                        || book.publisher().contains(token)))
                .toList());

    given(bookRepository.readAll(keyword, pageable)).willReturn(expected);

    // When
    Page<Book> results = bookService.searchWithKeyword(keyword, pageable);

    // Then
    assertThat(results.getContent())
        .allSatisfy(
            result -> {
              boolean contains =
                  keywordTokens.stream()
                      .anyMatch(
                          token ->
                              result.title().contains(token)
                                  || result.authorName().contains(token)
                                  || result.publisher().contains(token));

              assertThat(contains).isTrue();
            });
  }

  @Test
  @DisplayName("관심 카테고리에 해당하는 도서를 랜덤하게 조회한다.")
  void searchWithInterestedCategoriesTest() {
    // Given
    int count = 3;
    Member member = MemberFixture.create(127L, RoleType.ADMIN);
    List<Book> allBooks =
        IntStream.range(0, 5)
            .mapToObj(i -> BookFixture.create((long) i, MemberFixture.create(123L, RoleType.USER)))
            .toList();
    Page<Book> expected =
        new PageImpl<>(
            allBooks.stream()
                .filter(
                    book ->
                        member.interestedCategories().stream()
                            .anyMatch(category -> book.categories().contains(category)))
                .toList());

    given(memberRepository.read(member.memberId())).willReturn(member);
    given(bookRepository.readAll(anySet())).willReturn(expected);

    // When
    Page<Book> results = bookService.searchWithInterestedCategories(member.memberId());

    // Then
    assertThat(results)
        .hasSize((int) Math.min(count, expected.getTotalElements()))
        .allSatisfy(
            result ->
                assertThat(result.categories())
                    .containsAnyElementsOf(member.interestedCategories()));
  }
}
