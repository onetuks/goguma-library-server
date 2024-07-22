package com.onetuks.libraryapi.member.controller;

import com.onetuks.libraryapi.member.dto.request.MemberPatchRequest;
import com.onetuks.libraryapi.member.dto.response.MemberResponse;
import com.onetuks.libraryapi.member.dto.response.MemberSetResponses;
import com.onetuks.libraryauth.util.LoginId;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.member.service.MemberFacadeService;
import com.onetuks.librarydomain.member.service.MemberService;
import jakarta.validation.Valid;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/members")
public class MemberRestController {

  private final MemberService memberService;
  private final MemberFacadeService memberFacadeService;

  public MemberRestController(
      MemberService memberService, MemberFacadeService memberFacadeService) {
    this.memberService = memberService;
    this.memberFacadeService = memberFacadeService;
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
      path = "/{member-id}",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MemberResponse> patchMemberProfile(
      @LoginId Long loginId,
      @PathVariable(name = "member-id") Long memberId,
      @RequestPart(name = "request") @Valid MemberPatchRequest request,
      @RequestPart(name = "profile-image", required = false) MultipartFile profileImage,
      @RequestPart(name = "profile-background-image", required = false)
          MultipartFile profileBackgroundImage) {
    Member result =
        memberService.editProfile(
            loginId, memberId, request.to(), profileImage, profileBackgroundImage);
    MemberResponse response = MemberResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 멤버 프로필 단건 조회
   *
   * @param memberId : 조회할 멤버의 ID
   * @return : 조회된 멤버의 프로필 정보
   */
  @GetMapping(path = "/{member-id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<MemberResponse> getMemberProfile(
      @PathVariable(name = "member-id") Long memberId) {
    Member result = memberService.search(memberId);
    MemberResponse response = MemberResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 추천 멤버 프로필 조회 금주도서 기간동안 서평픽 많은 작가 7인 + 작성 서평 많은 작가 3인
   *
   * @return : 추천 멤버 프로필 목록
   */
  @GetMapping(path = "/recommend", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<MemberSetResponses> getRecommendedMemberProfiles() {
    Set<Member> results = memberFacadeService.searchAllForRecommend();
    MemberSetResponses responses = MemberSetResponses.from(results);

    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }
}
