package com.onetuks.libraryapi.review.controller;

import com.onetuks.libraryapi.review.dto.response.ReviewPickResponse;
import com.onetuks.libraryapi.review.dto.response.ReviewResponse.ReviewPageResponses;
import com.onetuks.libraryauth.util.LoginId;
import com.onetuks.librarydomain.review.model.ReviewPick;
import com.onetuks.librarydomain.review.service.ReviewPickService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/reviews")
public class ReviewPickRestController {

  private final ReviewPickService reviewPickService;

  public ReviewPickRestController(ReviewPickService reviewPickService) {
    this.reviewPickService = reviewPickService;
  }

  /**
   * 서평픽 등록
   *
   * <p>- 00시 기준 일간 5회만 포인트 지급
   *
   * <p>- 서평픽커 1포인트 지급
   *
   * <p>- 서평리시버 5포인트 지급
   *
   * @param loginId : 로그인 ID
   * @param reviewId : 서평 ID
   * @return : 서평픽 응답
   */
  @PostMapping(path = "/{review-id}/picks", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ReviewPickResponse> postNewReviewPick(
      @LoginId Long loginId, @PathVariable(name = "review-id") Long reviewId) {
    ReviewPick result = reviewPickService.register(loginId, reviewId);
    ReviewPickResponse response = ReviewPickResponse.from(result);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  /**
   * 서평픽 취소
   *
   * @param loginId : 로그인 ID
   * @param reviewPickId : 서평픽 ID
   * @return : 응답
   */
  @DeleteMapping(path = "/picks/{review-pick-id}")
  public ResponseEntity<Void> deleteReviewPick(
      @LoginId Long loginId, @PathVariable(name = "review-pick-id") Long reviewPickId) {
    reviewPickService.remove(loginId, reviewPickId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * 나의 서평픽 다건 조회
   *
   * @param loginId : 로그인 ID
   * @param pageable : 페이지 정보
   * @return : 서평픽 목록
   */
  @GetMapping(path = "/picks/my-picks", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ReviewPageResponses> getMyReviewPicks(
      @LoginId Long loginId,
      @PageableDefault(sort = "reviewPickId", direction = Direction.DESC) Pageable pageable) {
    Page<ReviewPick> results = reviewPickService.searchAll(loginId, pageable);
    ReviewPageResponses responses = ReviewPageResponses.fromPicks(results);

    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }

  /**
   * 서평픽 여부 조회
   *
   * @param loginId : 로그인 ID
   * @param reviewId : 서평 ID
   * @return : 서평픽 여부
   */
  @GetMapping(path = "/{review-id}/picks", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ReviewPickResponse> getMyReviewPick(
      @LoginId Long loginId, @PathVariable(name = "review-id") Long reviewId) {
    ReviewPick result = reviewPickService.searchExistence(loginId, reviewId);
    ReviewPickResponse response = ReviewPickResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 서평픽 카운트 조회
   *
   * @param loginId : 로그인 ID
   * @param reviewId : 서평 ID
   * @return : 서평픽 카운트
   */
  @GetMapping(path = "/{review-id}/picks/count", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Long> getMyReviewPickCount(
      @LoginId Long loginId, @PathVariable(name = "review-id") Long reviewId) {
    Long result = reviewPickService.searchCount(loginId, reviewId);

    return ResponseEntity.status(HttpStatus.OK).body(result);
  }
}
