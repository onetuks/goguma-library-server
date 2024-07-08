package com.onetuks.libraryapi.member.dto.response;

import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.member.model.MemberStatics;
import com.onetuks.libraryobject.enums.Category;
import java.util.Set;

public record MemberGetResponse(
    long memberId,
    String nickname,
    String introduction,
    Set<Category> interestedCategories,
    boolean isAlarmAccepted,
    long points,
    String profileImageUrl,
    String profileBackgroundImageUrl,
    MemberStatics memberStatics) {

  public static MemberGetResponse from(Member member) {
    return new MemberGetResponse(
        member.memberId(),
        member.nickname().value(),
        member.introduction(),
        member.interestedCategories(),
        member.isAlarmAccepted(),
        member.points(),
        member.profileImageFile().getUrl(),
        member.profileBackgroundImageFile().getUrl(),
        member.memberStatics());
  }
}
