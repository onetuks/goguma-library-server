package com.onetuks.libraryauth.exception;

import com.onetuks.libraryobject.error.ErrorCode;
import lombok.Getter;

@Getter
public class TokenIsLogoutException extends IllegalStateException {

  private final ErrorCode errorCode;

  public TokenIsLogoutException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
