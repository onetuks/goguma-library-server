package com.onetuks.librarydomain.global.point.producer.dto;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointEvent implements Serializable {

  private String creditType;
  private String activity;
  private long memberId;

  public PointEvent(String creditType, String activity, long memberId) {
    this.creditType = creditType;
    this.activity = activity;
    this.memberId = memberId;
  }
}
