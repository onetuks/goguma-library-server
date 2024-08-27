package com.onetuks.libraryapi.book.controller;

import com.onetuks.libraryapi.book.dto.response.BookPickResponse;
import com.onetuks.libraryapi.book.dto.response.BookResponse.BookResponses;
import com.onetuks.libraryauth.util.LoginId;
import com.onetuks.librarydomain.book.model.BookPick;
import com.onetuks.librarydomain.book.service.BookPickService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/books")
public class BookPickRestController {

  private final BookPickService bookPickService;

  public BookPickRestController(BookPickService bookPickService) {
    this.bookPickService = bookPickService;
  }

  /**
   * 북픽 등록
   *
   * @param loginId : 로그인 ID
   * @param bookId : 도서 ID
   * @return : 북픽 정보
   */
  @PostMapping(path = "/{book-id}/picks", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookPickResponse> postNewBookPick(
      @LoginId Long loginId, @PathVariable(name = "book-id") Long bookId) {
    BookPick result = bookPickService.register(loginId, bookId);
    BookPickResponse response = BookPickResponse.from(result);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  /**
   * 북픽 취소
   *
   * @param loginId : 로그인 ID
   * @param bookPickId : 북픽 ID
   * @return : 204 No Content
   */
  @DeleteMapping(path = "/{book-pick-id}")
  public ResponseEntity<Void> deleteMyBookPick(
      @LoginId Long loginId, @PathVariable(name = "book-pick-id") Long bookPickId) {
    bookPickService.remove(loginId, bookPickId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * 나의 북픽 도서 다건 조회
   *
   * @param loginId : 로그인 ID
   * @param pageable : 페이지 정보
   * @return : 북픽 도서 목록
   */
  @GetMapping(path = "/picks/my-picks", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookResponses> getMyBookPicks(
      @LoginId Long loginId, @PageableDefault(size = 3) Pageable pageable) {
    Page<BookPick> results = bookPickService.searchAll(loginId, pageable);
    BookResponses responses = BookResponses.fromPicks(results);

    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }

  /**
   * 북픽 여부 조회
   *
   * @param loginId : 로그인 ID
   * @param bookId : 북픽 ID
   * @return : 북픽 여부
   */
  @GetMapping(path = "/{book-id}/picks", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookPickResponse> getMyBookPick(
      @LoginId Long loginId, @PathVariable(name = "book-id") Long bookId) {
    BookPick result = bookPickService.searchExistence(loginId, bookId);
    BookPickResponse response = BookPickResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
