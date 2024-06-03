package com.onetuks.coreauth.controller.dto;

import com.onetuks.coreauth.service.dto.LoginResult;
import com.onetuks.libraryobject.enums.RoleType;
import java.util.List;

public record LoginResponse(
    String appToken, boolean isNewMember, long loginId, List<RoleType> roles) {

  public static LoginResponse from(LoginResult loginResult) {
    return new LoginResponse(
        loginResult.accessToken(),
        loginResult.isNewMember(),
        loginResult.loginId(),
        loginResult.roles());
  }
}
