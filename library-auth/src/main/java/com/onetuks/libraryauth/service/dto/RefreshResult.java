package com.onetuks.libraryauth.service.dto;

import com.onetuks.libraryobject.enums.RoleType;
import java.util.Set;

public record RefreshResult(String accessToken, Long loginId, Set<RoleType> roles) {

  public static RefreshResult of(String accessToken, Long loginId, Set<RoleType> roles) {
    return new RefreshResult(accessToken, loginId, roles);
  }
}
