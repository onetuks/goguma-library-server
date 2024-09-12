package com.onetuks.librarypoint.service.model.vo;

import lombok.Getter;

@Getter
public enum Activity {
  BOOK_REGISTRATION("도서 등록", 20L),
  REVIEW_REGISTRATION_EVENT("금주도서 서평 작성", 30L),
  REVIEW_REGISTRATION_BASE("일반도서 서평 작성", 15L),
  REVIEW_PICK_PICKER("서평픽 등록", 1L),
  REVIEW_PICK_RECEIVER("서평픽 수신", 5L),
  ATTENDANCE_DAILY("일일 출석", 1L),
  ATTENDANCE_3_DAYS("3일 연속 출석", 3L),
  ATTENDANCE_5_DAYS("5일 연속 출석", 5L),
  ATTENDANCE_10_DAYS("10일 연속 출석", 10L),
  ATTENDANCE_30_DAYS("30일 연속 출석", 30L),
  MEMBER_POINT_HISTORIES("포인트 내역 삭제", 0L);

  private final String description;
  private final long points;

  Activity(String description, long points) {
    this.description = description;
    this.points = points;
  }

  public String getNegativeDescription() {
    return this.description + " 취소";
  }

  public long getNegativePoints() {
    return -this.points;
  }
}
