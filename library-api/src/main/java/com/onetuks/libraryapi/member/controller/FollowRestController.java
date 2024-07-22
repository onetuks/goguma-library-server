package com.onetuks.libraryapi.member.controller;

import com.onetuks.libraryapi.member.dto.response.FollowResponse;
import com.onetuks.libraryauth.util.LoginId;
import com.onetuks.librarydomain.member.model.Follow;
import com.onetuks.librarydomain.member.service.FollowService;
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
  @PostMapping(path = "/members/follow-ship", produces = MediaType.APPLICATION_JSON_VALUE)
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
  @DeleteMapping(path = "/{follow-id}")
  public ResponseEntity<Void> deleteFollow(
      @LoginId Long loginId, @PathVariable(name = "follow-id") Long followId) {
    followService.remove(loginId, followId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * 팔로우 여부 조회
   * @param loginId : 로그인 아이디
   * @param followeeId : 팔로우 대상 아이디
   * @return : 팔로우 여부
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Boolean> getMyFollow(
      @LoginId Long loginId,
      @RequestParam(name = "followee-id") Long followeeId) {
    boolean result = followService.searchExistence(loginId, followeeId);

    return ResponseEntity.status(HttpStatus.OK).body(result);
  }

  @GetMapping(path = "/followers")
}
