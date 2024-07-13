package com.onetuks.libraryapi.review.controller;

import com.onetuks.libraryapi.review.dto.response.ReviewPickResponse;
import com.onetuks.libraryauth.util.LoginId;
import com.onetuks.librarydomain.review.model.ReviewPick;
import com.onetuks.librarydomain.review.service.ReviewPickService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/reviews/picks")
public class ReviewPickRestController {

  private final ReviewPickService reviewPickService;

  public ReviewPickRestController(ReviewPickService reviewPickService) {
    this.reviewPickService = reviewPickService;
  }

  /**
   * 서평픽 등록
   *
   * - 00시 기준 일간 5회만 포인트 지급
   * - 서평픽커 1포인트 지급
   * - 서평리시버 5포인트 지급
   *
   * @param loginId : 로그인 ID
   * @param reviewId : 서평 ID
   * @return : 서평픽 응답
   */
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ReviewPickResponse> postNewReviewPick(
      @LoginId Long loginId, @RequestParam(name = "review-id") Long reviewId) {
    ReviewPick result = reviewPickService.register(loginId, reviewId);
    ReviewPickResponse response = ReviewPickResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
