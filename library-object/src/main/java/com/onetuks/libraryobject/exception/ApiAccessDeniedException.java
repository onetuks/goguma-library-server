package com.onetuks.libraryobject.exception;

public class ApiAccessDeniedException extends IllegalArgumentException {

  public ApiAccessDeniedException() {}

  public ApiAccessDeniedException(String message) {
    super(message);
  }
}
