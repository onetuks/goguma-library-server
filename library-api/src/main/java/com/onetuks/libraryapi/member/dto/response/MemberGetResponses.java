package com.onetuks.libraryapi.member.dto.response;

import com.onetuks.librarydomain.member.model.Member;
import java.util.Set;
import java.util.stream.Collectors;

public record MemberGetResponses(Set<MemberGetResponse> responses) {

  public static MemberGetResponses from(Set<Member> results) {
    return new MemberGetResponses(
        results.stream().map(MemberGetResponse::from).collect(Collectors.toSet()));
  }
}
