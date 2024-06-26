package com.onetuks.libraryapi.member.controller;

import com.onetuks.libraryapi.member.controller.dto.request.MemberPatchRequest;
import com.onetuks.libraryapi.member.controller.dto.response.MemberGetResponse;
import com.onetuks.libraryapi.member.controller.dto.response.MemberPatchResponse;
import com.onetuks.libraryauth.util.LoginId;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/members")
public class MemberRestController {

  private final MemberService memberService;

  public MemberRestController(MemberService memberService) {
    this.memberService = memberService;
  }

  /**
   * 멤버 프로필 수정
   *
   * @param loginId : 로그인한 사용자의 ID
   * @param memberId : 수정할 멤버의 ID
   * @param request : 수정할 멤버의 정보
   * @return : 수정된 멤버의 정보
   */
  @PatchMapping(
      path = "/{memberId}",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<MemberPatchResponse> patchMemberProfile(
      @LoginId Long loginId,
      @PathVariable(name = "memberId") Long memberId,
      @RequestBody MemberPatchRequest request,
      @RequestPart(name = "profile-image", required = false) MultipartFile profileImage,
      @RequestPart(name = "profile-background-image", required = false)
          MultipartFile profileBackgroundImage) {
    Member result =
        memberService.updateMember(
            loginId, memberId, request.to(), profileImage, profileBackgroundImage);
    MemberPatchResponse response = MemberPatchResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 멤버 프로필 단건 조회
   * @param memberId : 조회할 멤버의 ID
   * @return : 조회된 멤버의 프로필 정보
   */
  @GetMapping(path = "/{memberId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<MemberGetResponse> getMemberProfile(
      @PathVariable(name = "memberId") Long memberId) {
    Member result = memberService.readMember(memberId);
    MemberGetResponse response = MemberGetResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
