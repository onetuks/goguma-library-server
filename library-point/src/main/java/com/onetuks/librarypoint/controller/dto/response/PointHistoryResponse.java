package com.onetuks.librarypoint.controller.dto.response;

import com.onetuks.librarypoint.service.model.PointHistory;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;

public record PointHistoryResponse(
    long pointHistoryId, long memberId, String activity, long points, LocalDateTime createdAt) {

  public static PointHistoryResponse from(PointHistory model) {
    return new PointHistoryResponse(
        model.pointHistoryId(),
        model.member().memberId(),
        model.activity(),
        model.points(),
        model.createdAt());
  }

  public record PointHistoryResponses(Page<PointHistoryResponse> responses) {

    public static PointHistoryResponses from(Page<PointHistory> results) {
      return new PointHistoryResponses(results.map(PointHistoryResponse::from));
    }
  }
}
