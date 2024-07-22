package com.onetuks.libraryapi.member.dto.response;

import com.onetuks.librarydomain.member.model.Member;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

public record MemberSetResponses(Set<MemberResponse> responses) {

  public static MemberSetResponses from(Set<Member> results) {
    return new MemberSetResponses(
        results.stream().map(MemberResponse::from).collect(Collectors.toSet()));
  }
}
