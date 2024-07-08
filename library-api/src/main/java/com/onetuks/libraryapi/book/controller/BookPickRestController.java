package com.onetuks.libraryapi.book.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = "/books/picks")
public class BookPickRestController {

  private final BookPickService bookPickService;

  public BookPickRestController(BookPickService bookPickService) {
    this.bookPickService = bookPickService;
  }

  /**
   * 북픽 도서 조회
   *
   * @param loginId : 로그인 ID
   * @param pageable : 페이지 정보
   * @return : 북픽 도서 목록
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookResponses> getMyBookPicks(
      @LoginId Long loginId, @PageableDefault(size = 3) Pageable pageable) {
    Page<BookPick> results = bookPickService.searchMyBookPicks(loginId, pageable);
    BookResponses responses = BookResponses.fromPicks(results);

    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }

  /**
   * 북픽 여부 조회
   *
   * @param loginId : 로그인 ID
   * @param bookPickId : 북픽 ID
   * @return : 북픽 여부
   */
  @GetMapping(path = "/{bookPickId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Boolean> getMyBookPick(
      @LoginId Long loginId, @PathVariable Long bookPickId) {
    boolean result = bookPickService.searchIsMyBookPick(loginId, bookPickId);

    return ResponseEntity.status(HttpStatus.OK).body(result);
  }
}
