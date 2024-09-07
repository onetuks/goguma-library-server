package com.onetuks.libraryobject.enums;

import static com.onetuks.libraryobject.enums.BadgeType.ATTENDANCE_BADGE;
import static com.onetuks.libraryobject.enums.BadgeType.FOLLOWER_BADGE;
import static com.onetuks.libraryobject.enums.BadgeType.REVIEW_BADGE;

import lombok.Getter;

@Getter
public enum Badge {
  // REVIEW BADGE
  REVIEW_NOOB(REVIEW_BADGE, "개털 서재", "서평 1회 작성", 1),
  REVIEW_APPRENTICE(REVIEW_BADGE, "이제 좀 볼만한 서재", "서평 5회 작성", 5),
  REVIEW_PRO(REVIEW_BADGE, "제법 좀 치는 서재", "서평 10회 작성", 10),
  REVIEW_EXPERT(REVIEW_BADGE, "열심히 해줘서 고마워 서재", "서평 20회 작성", 20),

  // FOLLOWER BADGE
  FOLLOWER_NOOB(FOLLOWER_BADGE, "차가운 고구마", "팔로워 1명 달성", 1),
  FOLLOWER_APPRENTICE(FOLLOWER_BADGE, "따뜻한 고구마", "팔로워 5명 달성", 5),
  FOLLOWER_PRO(FOLLOWER_BADGE, "조금 더 따뜻한 고구마", "팔로워 10명 달성", 10),
  FOLLOWER_EXPERT(FOLLOWER_BADGE, "앗뜨거 고구마", "팔로워 20명 달성", 20),

  // ATTENDANCE BADGE
  ATTENDANCE_NOOB(ATTENDANCE_BADGE, "얼굴도장", "출석 1회 달성", 1),
  ATTENDANCE_APPRENTICE(ATTENDANCE_BADGE, "인사하고 지내는 사이", "출석 5회 달성", 5),
  ATTENDANCE_PRO(ATTENDANCE_BADGE, "제법 친한 사이", "출석 10회 달성", 10),
  ATTENDANCE_EXPERT(ATTENDANCE_BADGE, "자주 보니 반가운 사이", "출석 20회 달성", 20);

  private final BadgeType badgeType;
  private final String badgeName;
  private final String badgeDescription;
  private final int badgeCondition;

  Badge(BadgeType badgeType, String badgeName, String badgeDescription, int badgeCondition) {
    this.badgeType = badgeType;
    this.badgeName = badgeName;
    this.badgeDescription = badgeDescription;
    this.badgeCondition = badgeCondition;
  }
}
