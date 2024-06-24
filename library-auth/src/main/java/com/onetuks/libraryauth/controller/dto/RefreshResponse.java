package com.onetuks.libraryauth.controller.dto;

import com.onetuks.libraryauth.service.dto.RefreshResult;

public record RefreshResponse(String accessToken, long loginId) {

  public static RefreshResponse from(RefreshResult refreshResult) {
    return new RefreshResponse(refreshResult.accessToken(), refreshResult.loginId());
  }
}
