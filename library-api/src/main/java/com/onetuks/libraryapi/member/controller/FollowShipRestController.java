package com.onetuks.libraryapi.member.controller;

import com.onetuks.libraryapi.member.dto.response.FollowShipResponse;
import com.onetuks.libraryauth.util.LoginId;
import com.onetuks.librarydomain.member.model.FollowShip;
import com.onetuks.librarydomain.member.service.FollowShipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/members/follow-ships")
public class FollowShipRestController {

  private final FollowShipService followShipService;

  public FollowShipRestController(FollowShipService followShipService) {
    this.followShipService = followShipService;
  }

  /**
   * 팔로우 등록
   *
   * @param loginId : 로그인 아이디
   * @param followeeId : 팔로우 대상 아이디
   * @return : 팔로우 등록 결과
   */
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<FollowShipResponse> postNewFollowShip(
      @LoginId Long loginId, @RequestParam(name = "followee-id") Long followeeId) {
    FollowShip result = followShipService.register(loginId, followeeId);
    FollowShipResponse response = FollowShipResponse.from(result);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  /**
   * 팔로우 취소
   *
   * @param loginId : 로그인 아이디
   * @param followShipId : 팔로우 식별자
   * @return : 204 No Content
   */
  @DeleteMapping(path = "/{follow-ship-id}")
  public ResponseEntity<Void> deleteFollowShip(
      @LoginId Long loginId, @PathVariable(name = "follow-ship-id") Long followShipId) {
    followShipService.remove(loginId, followShipId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
