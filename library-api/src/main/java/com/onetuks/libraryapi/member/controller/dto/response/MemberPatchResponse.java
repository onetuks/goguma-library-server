package com.onetuks.libraryapi.member.controller.dto.response;

import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.libraryobject.enums.Category;
import java.util.List;

public record MemberPatchResponse(
    long memberId,
    String nickname,
    String introduction,
    List<Category> interestedCategories,
    boolean isAlarmAccepted,
    String profileImageFileUrl,
    String profileBackgroundImageFileUrl) {

  public static MemberPatchResponse from(Member member) {
    return new MemberPatchResponse(
        member.memberId(),
        member.nickname().value(),
        member.introduction(),
        member.interestedCategories(),
        member.isAlarmAccepted(),
        member.profileImageFile().getUrl(),
        member.profileBackgroundImageFile().getUrl());
  }
}
