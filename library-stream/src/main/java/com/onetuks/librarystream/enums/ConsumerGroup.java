package com.onetuks.librarystream.enums;

import lombok.Getter;

@Getter
public enum ConsumerGroup {
  POINT_EVENT_GROUP("point-event-group");

  private final String group;

  ConsumerGroup(String group) {
    this.group = group;
  }
}
