package com.onetuks.libraryapi.book.controller;

import com.onetuks.libraryapi.book.dto.request.BookPostRequest;
import com.onetuks.libraryapi.book.dto.response.BookIsbnGetResponse;
import com.onetuks.libraryapi.book.dto.response.BookResponse;
import com.onetuks.libraryapi.book.dto.response.BookResponse.BookResponses;
import com.onetuks.libraryauth.util.LoginId;
import com.onetuks.librarydomain.book.handler.dto.IsbnResult;
import com.onetuks.librarydomain.book.model.Book;
import com.onetuks.librarydomain.book.service.BookService;
import com.onetuks.librarydomain.weekly.service.WeeklyFeaturedBookService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/api/books")
public class BookRestController {

  private static final Logger log = LoggerFactory.getLogger(BookRestController.class);

  private final BookService bookService;
  private final WeeklyFeaturedBookService weeklyFeaturedBookService;

  public BookRestController(
      BookService bookService, WeeklyFeaturedBookService weeklyFeaturedBookService) {
    this.bookService = bookService;
    this.weeklyFeaturedBookService = weeklyFeaturedBookService;
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
    IsbnResult result = bookService.search(isbn);
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

    log.info("[도서] 도서가 등록되었습니다 - bookId: {}", result.bookId());

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  /**
   * 도서 단건 조회
   *
   * @param bookId : 도서 ID
   * @return : 도서 정보
   */
  @GetMapping(path = "/{book-id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookResponse> getBook(@PathVariable(name = "book-id") Long bookId) {
    Book result = bookService.search(bookId);
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
    Page<Book> results = bookService.searchWithKeyword(keyword, pageable);
    BookResponses responses = BookResponses.from(results);

    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }

  /**
   * 관심 카테고리 포함 도서 다건 조회
   *
   * @param loginId : 로그인 ID
   * @return : 관심 카테고리 포함 도서 목록
   */
  @GetMapping(
      path = "/recommend/interested-categories",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookResponses> getBooksWithInterestedCategories(@LoginId Long loginId) {
    Page<Book> results = bookService.searchWithInterestedCategories(loginId);
    BookResponses responses = BookResponses.from(results);

    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }

  /**
   * 금주도서 조회
   *
   * @return : 금주 도서 목록
   */
  @GetMapping(path = "/recommend/weekly-featured", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookResponses> getWeeklyBooks() {
    Page<Book> results = weeklyFeaturedBookService.searchAllForThisWeek();
    BookResponses responses = BookResponses.from(results);

    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }
}
