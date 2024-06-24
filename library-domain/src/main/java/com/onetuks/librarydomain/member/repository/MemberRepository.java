package com.onetuks.librarydomain.member.repository;

import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.libraryobject.enums.ClientProvider;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository {

  Member create(Member member);

  Member read(long memberId);

  Optional<Member> read(String social, ClientProvider clientProvider);

  Member update(Member member);

  void delete(long memberId);
}
