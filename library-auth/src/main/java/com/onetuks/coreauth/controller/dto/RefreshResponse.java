package com.onetuks.coreauth.controller.dto;

import com.onetuks.coreauth.service.dto.RefreshResult;

public record RefreshResponse(String accessToken, long loginId) {

  public static RefreshResponse from(RefreshResult refreshResult) {
    return new RefreshResponse(refreshResult.accessToken(), refreshResult.loginId());
  }
}
