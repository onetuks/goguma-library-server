package com.onetuks.libraryobject.exception;

public class UniqueColumnConstraintException extends IllegalArgumentException {

  public UniqueColumnConstraintException() {}

  public UniqueColumnConstraintException(String message) {
    super(message);
  }
}
