package com.onetuks.libraryapi.book.controller;

import com.onetuks.libraryapi.book.dto.request.BookPatchRequest;
import com.onetuks.libraryapi.book.dto.request.BookPostRequest;
import com.onetuks.libraryapi.book.dto.response.BookIsbnGetResponse;
import com.onetuks.libraryapi.book.dto.response.BookResponse;
import com.onetuks.libraryapi.book.dto.response.BookResponse.BookResponses;
import com.onetuks.libraryauth.util.LoginId;
import com.onetuks.libraryauth.util.OnlyForAdmin;
import com.onetuks.librarydomain.book.model.Book;
import com.onetuks.librarydomain.book.service.BookService;
import com.onetuks.libraryexternal.book.handler.dto.IsbnResult;
import com.onetuks.libraryexternal.book.service.IsbnSearchService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/books")
public class BookRestController {

  private final BookService bookService;
  private final IsbnSearchService isbnSearchService;

  public BookRestController(BookService bookService, IsbnSearchService isbnSearchService) {
    this.bookService = bookService;
    this.isbnSearchService = isbnSearchService;
  }

  /**
   * ISBN 도서 정보 조회
   *
   * @param isbn : ISBN
   * @return : 도서 정보
   */
  @GetMapping(path = "/isbn/{isbn}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookIsbnGetResponse> getBookWithIsbn(
      @PathVariable(name = "isbn") String isbn) {
    IsbnResult result = isbnSearchService.search(isbn);
    BookIsbnGetResponse response = BookIsbnGetResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 도서 등록
   *
   * @param request : 도서 등록 요청
   * @return : 등록 도서 정보
   */
  @PostMapping(
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookResponse> postNewBook(
      @LoginId Long loginId,
      @RequestPart(name = "request") @Valid BookPostRequest request,
      @RequestPart(name = "cover-image", required = false) MultipartFile coverImage) {
    Book result = bookService.register(loginId, request.to(), coverImage);
    BookResponse response = BookResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 도서 정보 수정
   *
   * @param bookId : 도서 ID
   * @param request : 도서 수정 요청
   * @param coverImage : 표지 이미지
   * @return : 수정된 도서 정보
   */
  @OnlyForAdmin
  @PatchMapping(
      path = "/admin/{bookId}",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookResponse> patchBook(
      @PathVariable(name = "bookId") Long bookId,
      @RequestPart(name = "request") @Valid BookPatchRequest request,
      @RequestPart(name = "cover-image", required = false) MultipartFile coverImage) {
    Book result = bookService.edit(bookId, request.to(), coverImage);
    BookResponse response = BookResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 도서 삭제
   *
   * @param bookId : 도서 ID
   * @return : 204 No Content
   */
  @OnlyForAdmin
  @DeleteMapping(path = "/admin/{bookId}")
  public ResponseEntity<Void> deleteBook(@PathVariable(name = "bookId") Long bookId) {
    bookService.remove(bookId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * 검수 대상 도서 다건 조회
   *
   * @param inspectionMode : 검수 모드
   * @return : 검수 대상 도서 목록
   */
  @OnlyForAdmin
  @GetMapping(path = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookResponses> getBooksForInspection(
      @RequestParam(name = "inspection-mode", required = false, defaultValue = "true")
          Boolean inspectionMode,
      @PageableDefault(sort = "book.bookId", direction = Direction.DESC) Pageable pageable) {
    Page<Book> results = bookService.findAll(inspectionMode, pageable);
    BookResponses responses = BookResponses.from(results);

    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }

  /**
   * 도서 단건 조회
   *
   * @param bookId : 도서 ID
   * @return : 도서 정보
   */
  @GetMapping(path = "/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookResponse> getBook(@PathVariable(name = "bookId") Long bookId) {
    Book result = bookService.find(bookId);
    BookResponse response = BookResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 제목/저자/출판사 포함 도서 다건 조회
   *
   * @param keyword : 검색 키워드
   * @return : 검색 결과 도서 목록
   */
  @GetMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookResponses> getBooksWithKeyword(
      @RequestParam(name = "keyword", required = false) String keyword,
      @PageableDefault Pageable pageable) {
    Page<Book> results = bookService.findAll(keyword, pageable);
    BookResponses responses = BookResponses.from(results);

    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }

  /**
   * 관심 카테고리 포함 도서 다건 조회
   *
   * @param loginId : 로그인 ID
   * @param pageable : 페이지 정보
   * @return : 관심 카테고리 포함 도서 목록
   */
  @GetMapping(
      path = "/recommend/interested-categories",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookResponses> getBooksWithInterestedCategories(
      @LoginId Long loginId,
      @PageableDefault(size = 3) Pageable pageable) {
    Page<Book> results = bookService.findAll(loginId, pageable);
    BookResponses responses = BookResponses.from(results);

    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }
}
