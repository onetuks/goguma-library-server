package com.onetuks.librarystream.enums;

import lombok.Getter;

@Getter
public enum StreamKey {
  POINT_EVENT("point_event");

  private final String key;

  StreamKey(String key) {
    this.key = key;
  }
}
