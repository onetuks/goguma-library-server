package com.onetuks.libraryapi.book.controller;

import com.onetuks.libraryapi.book.dto.request.BookPatchRequest;
import com.onetuks.libraryapi.book.dto.response.BookResponse;
import com.onetuks.libraryapi.book.dto.response.BookResponse.BookResponses;
import com.onetuks.libraryauth.util.OnlyForAdmin;
import com.onetuks.librarydomain.book.model.Book;
import com.onetuks.librarydomain.book.service.BookService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/api/admin/books")
public class AdminBookRestController {

  private final BookService bookService;

  public AdminBookRestController(BookService bookService) {
    this.bookService = bookService;
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
      path = "/{book-id}",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookResponse> patchBook(
      @PathVariable(name = "book-id") Long bookId,
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
  @DeleteMapping(path = "/{book-id}")
  public ResponseEntity<Void> deleteBook(@PathVariable(name = "book-id") Long bookId) {
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
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookResponses> getBooksForInspection(
      @RequestParam(name = "inspection-mode", required = false, defaultValue = "true")
          Boolean inspectionMode,
      @PageableDefault(sort = "book.bookId", direction = Direction.DESC) Pageable pageable) {
    Page<Book> results = bookService.searchForInspection(inspectionMode, pageable);
    BookResponses responses = BookResponses.from(results);

    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }
}
