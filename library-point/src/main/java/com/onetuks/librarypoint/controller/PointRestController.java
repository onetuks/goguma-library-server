package com.onetuks.librarypoint.controller;

import com.onetuks.libraryauth.util.LoginId;
import com.onetuks.librarydomain.global.point.service.PointService;
import com.onetuks.librarypoint.controller.dto.response.PointHistoryResponse.PointHistoryResponses;
import com.onetuks.librarypoint.service.PointServiceImpl;
import com.onetuks.librarypoint.service.model.PointHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/points")
public class PointRestController {

  private final PointServiceImpl pointService;

  public PointRestController(PointServiceImpl pointService) {
    this.pointService = pointService;
  }

  /**
   * 포인트 내역 다건 조회
   *
   * @param loginId  : 로그인 ID
   * @param pageable : 페이지 정보
   * @return 포인트 내역 응답
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PointHistoryResponses> getPointHistories(
      @LoginId Long loginId,
      @PageableDefault(sort = "createdAt") Pageable pageable) {
    Page<PointHistory> results = pointService.searchAllPointHistories(loginId, pageable);
    PointHistoryResponses responses = PointHistoryResponses.from(results);

    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }
}
