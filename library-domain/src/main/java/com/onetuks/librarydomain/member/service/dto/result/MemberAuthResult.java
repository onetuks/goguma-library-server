package com.onetuks.librarydomain.member.service.dto.result;

import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.libraryobject.enums.RoleType;
import java.util.Set;

public record MemberAuthResult(boolean isNewMember, long memberId, Set<RoleType> roles) {

  public static MemberAuthResult of(boolean isNewMember, Member member) {
    return new MemberAuthResult(isNewMember, member.memberId(), member.authInfo().roles());
  }
}
