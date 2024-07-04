package com.onetuks.libraryauth.controller.dto;

import com.onetuks.libraryauth.service.dto.RefreshResult;
import com.onetuks.libraryobject.enums.RoleType;
import java.util.List;

public record RefreshResponse(String accessToken, long loginId, List<RoleType> roles) {

  public static RefreshResponse from(RefreshResult refreshResult) {
    return new RefreshResponse(
        refreshResult.accessToken(), refreshResult.loginId(), refreshResult.roles());
  }
}
