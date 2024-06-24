package com.onetuks.libraryauth.controller.dto;

import com.onetuks.libraryauth.service.dto.LoginResult;
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
