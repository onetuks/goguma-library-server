package com.onetuks.librarydomain.member.service;

import com.onetuks.librarydomain.file.FileRepository;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.member.model.vo.AuthInfo;
import com.onetuks.librarydomain.member.repository.MemberRepository;
import com.onetuks.librarydomain.member.service.dto.result.MemberAuthResult;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

  private final MemberRepository memberRepository;
  private final FileRepository fileRepository;

  public MemberService(MemberRepository memberRepository, FileRepository fileRepository) {
    this.memberRepository = memberRepository;
    this.fileRepository = fileRepository;
  }

  @Transactional
  public MemberAuthResult createMemberIfNotExists(AuthInfo authInfo) {
    Optional<Member> optionalMember =
        memberRepository.read(authInfo.socialId(), authInfo.clientProvider());

    Member savedMember =
        optionalMember.orElseGet(
            () ->
                memberRepository.create(
                    Member.builder().authInfo(authInfo).points(0L).isAlarmAccepted(true).build()));

    return MemberAuthResult.of(optionalMember.isEmpty(), savedMember);
  }

  @Transactional
  public void deleteMember(Long memberId) {
    Member member = memberRepository.read(memberId);

    fileRepository.deleteFile(member.profileImageFile().getUri());
    memberRepository.delete(memberId);
  }
}
