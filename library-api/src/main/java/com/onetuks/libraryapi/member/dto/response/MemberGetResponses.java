package com.onetuks.libraryapi.member.dto.response;

import com.onetuks.librarydomain.member.model.Member;
import java.util.List;

public record MemberGetResponses(List<MemberGetResponse> responses) {

  public static MemberGetResponses from(List<Member> results) {
    return new MemberGetResponses(results.stream().map(MemberGetResponse::from).toList());
  }
}
