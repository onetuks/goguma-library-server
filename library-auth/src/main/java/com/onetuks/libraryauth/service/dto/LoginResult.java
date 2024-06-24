package com.onetuks.libraryauth.service.dto;

import com.onetuks.libraryobject.enums.RoleType;
import java.util.List;

public record LoginResult(
    String accessToken, boolean isNewMember, long loginId, List<RoleType> roles) {

  public static LoginResult of(
      String accessToken, boolean isNewMember, Long loginId, List<RoleType> roles) {
    return new LoginResult(accessToken, isNewMember, loginId, roles);
  }
}
