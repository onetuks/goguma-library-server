package com.onetuks.libraryapi.review.controller;

import com.onetuks.libraryapi.review.dto.request.ReviewRequest;
import com.onetuks.libraryapi.review.dto.response.ReviewResponse;
import com.onetuks.libraryapi.review.dto.response.ReviewResponse.ReviewResponses;
import com.onetuks.libraryauth.util.LoginId;
import com.onetuks.librarydomain.review.model.Review;
import com.onetuks.librarydomain.review.service.ReviewService;
import com.onetuks.libraryobject.enums.ReviewSortBy;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/reviews")
public class ReviewRestController {

  private final ReviewService reviewService;

  public ReviewRestController(ReviewService reviewService) {
    this.reviewService = reviewService;
  }

  /**
   * 서평 등록
   *
   * @param loginId : 로그인 ID
   * @param request : 서평 등록 요청
   * @return : 서평 정보
   */
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ReviewResponse> postNewReview(
      @LoginId Long loginId,
      @RequestParam(name = "book-id") Long bookId,
      @RequestBody ReviewRequest request) {
    Review result = reviewService.register(loginId, bookId, request.to());
    ReviewResponse response = ReviewResponse.from(result);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  /**
   * 서평 수정
   *
   * @param loginId : 로그인 ID
   * @param reviewId : 서평 ID
   * @param request : 서평 수정 요청
   * @return : 서평 정보
   */
  @PatchMapping(
      path = "/{review-id}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ReviewResponse> patchReview(
      @LoginId Long loginId,
      @PathVariable(name = "review-id") Long reviewId,
      @RequestBody ReviewRequest request) {
    Review result = reviewService.edit(loginId, reviewId, request.to());
    ReviewResponse response = ReviewResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 서평 삭제
   *
   * @param loginId : 로그인 ID
   * @param reviewId : 서평 ID
   * @return : 204 No Content
   */
  @DeleteMapping(path = "{review-id}")
  public ResponseEntity<Void> deleteReview(
      @LoginId Long loginId, @PathVariable(name = "review-id") Long reviewId) {
    reviewService.remove(loginId, reviewId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * 서평 단건 조회
   *
   * @param reviewId : 서평 ID
   * @return : 서평 정보
   */
  @GetMapping(path = "{review-id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ReviewResponse> getReview(@PathVariable(name = "review-id") Long reviewId) {
    Review result = reviewService.search(reviewId);
    ReviewResponse response = ReviewResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 서평 다건 조회
   *
   * @param reviewSortBy : 정렬 기준
   * @param pageable : 페이징 정보
   * @return : 서평 목록
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ReviewResponses> getReviews(
      @RequestParam(name = "sort", required = false, defaultValue = "LATEST")
          ReviewSortBy reviewSortBy,
      @PageableDefault(sort = "reviewId", direction = Direction.DESC) Pageable pageable) {
    Page<Review> results = reviewService.searchAll(reviewSortBy, pageable);
    ReviewResponses responses = ReviewResponses.from(results);

    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }
}
