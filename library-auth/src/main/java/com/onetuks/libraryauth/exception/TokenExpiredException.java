package com.onetuks.libraryauth.exception;

import com.onetuks.libraryobject.error.ErrorCode;
import lombok.Getter;

@Getter
public class TokenExpiredException extends IllegalStateException {

  private final ErrorCode errorCode;

  public TokenExpiredException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
