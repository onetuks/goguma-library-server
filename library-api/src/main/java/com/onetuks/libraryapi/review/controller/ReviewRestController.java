package com.onetuks.libraryapi.review.controller;

import com.onetuks.libraryapi.review.dto.request.ReviewRequest;
import com.onetuks.libraryapi.review.dto.response.ReviewResponse;
import com.onetuks.libraryauth.util.LoginId;
import com.onetuks.librarydomain.review.model.Review;
import com.onetuks.librarydomain.review.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
}
