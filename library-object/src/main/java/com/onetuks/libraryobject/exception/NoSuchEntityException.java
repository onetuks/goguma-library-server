package com.onetuks.libraryobject.exception;

public class NoSuchEntityException extends RuntimeException{

  public NoSuchEntityException() {
  }

  public NoSuchEntityException(String message) {
    super(message);
  }
}
