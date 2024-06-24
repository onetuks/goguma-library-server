package com.onetuks.libraryauth.controller.dto;

import com.onetuks.libraryauth.service.dto.LogoutResult;

public record LogoutResponse(boolean isLogout) {

  public static LogoutResponse from(LogoutResult logoutResult) {
    return new LogoutResponse(logoutResult.islogout());
  }
}
