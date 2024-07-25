package com.onetuks.libraryapi.member.dto.response;

import com.onetuks.librarydomain.member.model.Member;
import org.springframework.data.domain.Page;

public record MemberPageResponses(Page<MemberResponse> responses) {

  public static MemberPageResponses from(Page<Member> results) {
    return new MemberPageResponses(results.map(MemberResponse::from));
  }
}
