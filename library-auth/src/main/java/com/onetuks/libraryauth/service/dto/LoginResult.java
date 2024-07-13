package com.onetuks.libraryauth.service.dto;

import com.onetuks.libraryobject.enums.RoleType;
import java.util.Set;

public record LoginResult(
    String accessToken, boolean isNewMember, long loginId, Set<RoleType> roles) {

  public static LoginResult of(
      String accessToken, boolean isNewMember, Long loginId, Set<RoleType> roles) {
    return new LoginResult(accessToken, isNewMember, loginId, roles);
  }
}
