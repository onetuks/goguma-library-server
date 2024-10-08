package com.onetuks.librarydomain.member.service;

import com.onetuks.librarydomain.global.file.repository.FileRepository;
import com.onetuks.librarydomain.global.point.producer.PointEventProducer;
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

  private final PointEventProducer pointEventProducer;

  public MemberService(
      MemberRepository memberRepository,
      FileRepository fileRepository,
      PointEventProducer pointEventProducer) {
    this.memberRepository = memberRepository;
    this.fileRepository = fileRepository;
    this.pointEventProducer = pointEventProducer;
  }

  @Transactional
  public MemberAuthResult registerIfNotExists(AuthInfo authInfo) {
    Optional<Member> optionalMember =
        memberRepository.read(authInfo.socialId(), authInfo.clientProvider());

    Member savedMember =
        optionalMember.orElseGet(
            () ->
                memberRepository.create(
                    Member.builder()
                        .authInfo(authInfo)
                        .points(0L)
                        .badges(Set.of())
                        .isAlarmAccepted(true)
                        .build()));

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
                param.instagramUrl(),
                param.interestedCategories(),
                param.isAlarmAccepted(),
                param.profileImageFilename(),
                param.profileBackgroundImageFilename(),
                profileImage,
                profileBackgroundImage);

    fileRepository.putFile(member.profileImageFile());
    fileRepository.putFile(member.profileBackgroundImageFile());

    return memberRepository.update(member);
  }

  @Transactional
  public boolean remove(long memberId) {
    Member member = memberRepository.read(memberId);

    pointEventProducer.removeMemberPointHistories(memberId);

    fileRepository.deleteFile(member.profileImageFile());
    memberRepository.delete(memberId);

    return true;
  }
}
