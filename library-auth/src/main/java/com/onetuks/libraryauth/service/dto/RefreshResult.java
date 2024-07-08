package com.onetuks.libraryauth.service.dto;

import com.onetuks.libraryobject.enums.RoleType;
import java.util.List;

public record RefreshResult(String accessToken, Long loginId, List<RoleType> roles) {

  public static RefreshResult of(String accessToken, Long loginId, List<RoleType> roles) {
    return new RefreshResult(accessToken, loginId, roles);
  }
}
