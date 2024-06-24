package com.onetuks.libraryobject.enums;

import lombok.Getter;

@Getter
public enum ImageType {
  PROFILE_IMAGE("/profiles/"),
  COVER_IMAGE("/covers/");

  private final String directoryPath;

  ImageType(String directoryPath) {
    this.directoryPath = directoryPath;
  }
}
