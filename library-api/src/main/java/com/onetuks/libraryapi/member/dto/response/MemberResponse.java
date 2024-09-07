package com.onetuks.libraryapi.member.dto.response;

import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.member.model.MemberStatics;
import com.onetuks.libraryobject.enums.Badge;
import com.onetuks.libraryobject.enums.Category;
import java.util.Set;

public record MemberResponse(
    long memberId,
    String nickname,
    String introduction,
    String instagramUrl,
    Set<Category> interestedCategories,
    boolean isAlarmAccepted,
    long points,
    Set<Badge> badges,
    String profileImageUrl,
    String profileBackgroundImageUrl,
    MemberStatics memberStatics) {

  public static MemberResponse from(Member member) {
    return new MemberResponse(
        member.memberId(),
        member.nickname().value(),
        member.introduction(),
        member.instagramUrl(),
        member.interestedCategories(),
        member.isAlarmAccepted(),
        member.points(),
        member.badges(),
        member.profileImageFile().getUrl(),
        member.profileBackgroundImageFile().getUrl(),
        member.memberStatics());
  }
}
