package com.onetuks.coreauth.service.dto;

public record RefreshResult(String accessToken, Long loginId) {

  public static RefreshResult of(String accessToken, Long loginId) {
    return new RefreshResult(accessToken, loginId);
  }
}
