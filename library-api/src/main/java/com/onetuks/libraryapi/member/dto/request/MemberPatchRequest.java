package com.onetuks.libraryapi.member.dto.request;

import com.onetuks.librarydomain.member.service.dto.param.MemberProfileParam;
import com.onetuks.libraryobject.enums.Category;
import java.util.List;

public record MemberPatchRequest(
    String nickname,
    String introduction,
    List<Category> interestedCategories,
    boolean isAlarmAccepted) {

  public MemberProfileParam to() {
    return new MemberProfileParam(
        this.nickname(), this.introduction(), this.interestedCategories(), this.isAlarmAccepted());
  }
}
