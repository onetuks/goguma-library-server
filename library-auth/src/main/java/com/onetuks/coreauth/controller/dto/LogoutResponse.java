package com.onetuks.coreauth.controller.dto;

import com.onetuks.coreauth.service.dto.LogoutResult;

public record LogoutResponse(boolean isLogout) {

  public static LogoutResponse from(LogoutResult logoutResult) {
    return new LogoutResponse(logoutResult.islogout());
  }
}
