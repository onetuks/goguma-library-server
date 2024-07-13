package com.onetuks.librarydomain.member.service;

import com.onetuks.librarydomain.file.FileRepository;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.member.model.vo.AuthInfo;
import com.onetuks.librarydomain.member.repository.MemberRepository;
import com.onetuks.librarydomain.member.service.dto.param.MemberProfileParam;
import com.onetuks.librarydomain.member.service.dto.result.MemberAuthResult;
import com.onetuks.libraryobject.enums.RoleType;
import com.onetuks.libraryobject.exception.ApiAccessDeniedException;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MemberService {

  private final MemberRepository memberRepository;
  private final FileRepository fileRepository;

  public MemberService(MemberRepository memberRepository, FileRepository fileRepository) {
    this.memberRepository = memberRepository;
    this.fileRepository = fileRepository;
  }

  @Transactional
  public MemberAuthResult registerIfNotExists(AuthInfo authInfo) {
    Optional<Member> optionalMember =
        memberRepository.read(authInfo.socialId(), authInfo.clientProvider());

    Member savedMember =
        optionalMember.orElseGet(
            () ->
                memberRepository.create(
                    Member.builder().authInfo(authInfo).points(0L).isAlarmAccepted(true).build()));

    return MemberAuthResult.of(optionalMember.isEmpty(), savedMember);
  }

  @Transactional(readOnly = true)
  public Member search(Long memberId) {
    return memberRepository.read(memberId);
  }

  @Transactional
  public Member editAuthorities(Long loginId, Set<RoleType> newRoles) {
    return memberRepository.update(memberRepository.read(loginId).changeRoles(newRoles));
  }

  @Transactional
  public Member editProfile(
      long loginId,
      long memberId,
      MemberProfileParam param,
      MultipartFile profileImage,
      MultipartFile profileBackgroundImage) {
    if (loginId != memberId) {
      throw new ApiAccessDeniedException("해당 유저에게 권한이 없는 요청입니다.");
    }

    Member member =
        memberRepository
            .read(memberId)
            .changeProfile(
                param.nickname(),
                param.introduction(),
                param.interestedCategories(),
                param.isAlarmAccepted(),
                profileImage,
                profileBackgroundImage);

    fileRepository.putFile(member.profileImageFile());
    fileRepository.putFile(member.profileBackgroundImageFile());

    return memberRepository.update(member);
  }

  @Transactional
  public void remove(long memberId) {
    Member member = memberRepository.read(memberId);

    fileRepository.deleteFile(member.profileImageFile());
    memberRepository.delete(memberId);
  }
}
