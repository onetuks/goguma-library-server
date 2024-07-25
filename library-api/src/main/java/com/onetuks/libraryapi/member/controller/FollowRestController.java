package com.onetuks.libraryapi.member.controller;

import com.onetuks.libraryapi.member.dto.response.FollowResponse;
import com.onetuks.libraryapi.member.dto.response.MemberPageResponses;
import com.onetuks.libraryauth.util.LoginId;
import com.onetuks.librarydomain.member.model.Follow;
import com.onetuks.librarydomain.member.model.Member;
import com.onetuks.librarydomain.member.service.FollowService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FollowRestController {

  private final FollowService followService;

  public FollowRestController(FollowService followService) {
    this.followService = followService;
  }

  /**
   * 팔로우 등록
   *
   * @param loginId : 로그인 아이디
   * @param followeeId : 팔로우 대상 아이디
   * @return : 팔로우 등록 결과
   */
  @PostMapping(path = "/members/follows", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<FollowResponse> postNewFollow(
      @LoginId Long loginId, @RequestParam(name = "followee-id") Long followeeId) {
    Follow result = followService.register(loginId, followeeId);
    FollowResponse response = FollowResponse.from(result);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  /**
   * 팔로우 취소
   *
   * @param loginId : 로그인 아이디
   * @param followId : 팔로우 식별자
   * @return : 204 No Content
   */
  @DeleteMapping(path = "/members/follows/{follow-id}")
  public ResponseEntity<Void> deleteFollow(
      @LoginId Long loginId, @PathVariable(name = "follow-id") Long followId) {
    followService.remove(loginId, followId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * 팔로우 여부 조회
   *
   * @param loginId : 로그인 아이디
   * @param followeeId : 팔로우 대상 아이디
   * @return : 팔로우 여부
   */
  @GetMapping(path = "/members/follows", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Boolean> getMyFollow(
      @LoginId Long loginId, @RequestParam(name = "followee-id") Long followeeId) {
    boolean result = followService.searchExistence(loginId, followeeId);

    return ResponseEntity.status(HttpStatus.OK).body(result);
  }

  /**
   * 팔로워(나를 팔로우하는 멤버) 목록 조회
   *
   * @param memberId : 멤버 식별자
   * @return : 팔로워 목록
   */
  @GetMapping(path = "/members/{member-id}/followers", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<MemberPageResponses> getFollowers(
      @PathVariable(name = "member-id") Long memberId,
      @PageableDefault(sort = "followId", direction = Direction.DESC) Pageable pageable) {
    Page<Member> results = followService.searchAllFollowers(memberId, pageable);
    MemberPageResponses responses = MemberPageResponses.from(results);

    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }

  @GetMapping(path = "/members/{member-id}/followings", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<MemberPageResponses> getFollowings(
      @PathVariable(name = "member-id") Long memberId,
      @PageableDefault(sort = "followId", direction = Direction.DESC) Pageable pageable) {
    Page<Member> results = followService.searchAllFollowings(memberId, pageable);
    MemberPageResponses responses = MemberPageResponses.from(results);

    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }
}
