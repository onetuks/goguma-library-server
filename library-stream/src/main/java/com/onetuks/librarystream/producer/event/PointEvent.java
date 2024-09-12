package com.onetuks.librarystream.producer.event;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointEvent implements MessageEvent {

  private String creditType;
  private String activity;
  private String memberId;

  public PointEvent(String creditType, String activity, long memberId) {
    this.creditType = creditType;
    this.activity = activity;
    this.memberId = String.valueOf(memberId);
  }

  public PointEvent(String creditType, String activity, String memberId) {
    this.creditType = creditType;
    this.activity = activity;
    this.memberId = memberId;
  }

  public static PointEvent of(Map<String, String> value) {
    Map<String, String> regulatedValue = new ConcurrentHashMap<>();

    value
        .keySet()
        .forEach(
            key -> {
              if (key.contains("creditType")) {
                String[] creditTypeValue = value.get(key).split(" ");
                regulatedValue.put("creditType", creditTypeValue[1]);
              } else if (key.contains("activity")) {
                String[] activityValue = value.get(key).split(" ");
                regulatedValue.put("activity", activityValue[1]);
              } else if (key.contains("memberId")) {
                String[] memberIdValue = value.get(key).split(" ");
                regulatedValue.put("memberId", memberIdValue[1]);
              }
            });

    return new PointEvent(
        regulatedValue.get("creditType"),
        regulatedValue.get("activity"),
        regulatedValue.get("memberId"));
  }

  @Override
  public String toString() {
    return "PointEvent{"
        + "creditType='"
        + creditType
        + '\''
        + ", activity='"
        + activity
        + '\''
        + ", memberId="
        + memberId
        + '}';
  }
}
