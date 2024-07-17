package com.onetuks.libraryauth.controller;

import static com.onetuks.libraryobject.enums.ClientProvider.GOOGLE;
import static com.onetuks.libraryobject.enums.ClientProvider.KAKAO;
import static com.onetuks.libraryobject.enums.ClientProvider.NAVER;

import com.onetuks.libraryauth.controller.dto.LoginResponse;
import com.onetuks.libraryauth.controller.dto.LogoutResponse;
import com.onetuks.libraryauth.controller.dto.RefreshResponse;
import com.onetuks.libraryauth.jwt.util.AuthHeaderUtil;
import com.onetuks.libraryauth.service.AuthService;
import com.onetuks.libraryauth.service.dto.LoginResult;
import com.onetuks.libraryauth.service.dto.LogoutResult;
import com.onetuks.libraryauth.service.dto.RefreshResult;
import com.onetuks.libraryauth.util.LoginId;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
public class AuthRestController {

  private final AuthService authService;

  public AuthRestController(AuthService authService) {
    this.authService = authService;
  }

  /**
   * 포스트맨 카카오 로그인 1. 포스트맨에서 카카오 토큰 수령 2. 서버에 Header Authentication : Bearer {토큰} 전송 3. 서버 JWT 토큰 발급
   *
   * @param request : 카카오 토큰
   * @return : 로그인 응답
   */
  @PostMapping(path = "/postman/kakao")
  public ResponseEntity<LoginResponse> kakaoLoginWithAuthToken(HttpServletRequest request) {
    LoginResult result =
        authService.loginWithClientAuthToken(
            KAKAO, AuthHeaderUtil.getAuthorizationHeaderValue(request));

    return ResponseEntity.status(HttpStatus.OK).body(LoginResponse.from(result));
  }

  /**
   * 클라이언트 카카오 로그인 1. 클라이언트에서 카카오 인가코드 수령 2. 서버에 Header Authentication: Bearer {인가코드} 전송 3. 서버 JWT
   * 토큰 발급
   *
   * @param request : 카카오 인가코드
   * @return : 로그인 응답
   */
  @PostMapping(path = "/kakao")
  public ResponseEntity<LoginResponse> kakaoLoginWithAuthCode(HttpServletRequest request) {
    LoginResult result =
        authService.loginWithClientAuthCode(
            KAKAO, AuthHeaderUtil.getAuthorizationHeaderValue(request));

    return ResponseEntity.status(HttpStatus.OK).body(LoginResponse.from(result));
  }

  /**
   * 포스트맨 구글 로그인 1. 포스트맨에서 구글 토큰 수령 2. 서버에 Header Authentication : Bearer {토큰} 전송 3. 서버 JWT 토큰 발급
   *
   * @param request : 구글 토큰
   * @return : 로그인 응답
   */
  @PostMapping(path = "/postman/google")
  public ResponseEntity<LoginResponse> googleLoginWithAuthToken(HttpServletRequest request) {
    LoginResult result =
        authService.loginWithClientAuthToken(
            GOOGLE, AuthHeaderUtil.getAuthorizationHeaderValue(request));

    return ResponseEntity.status(HttpStatus.OK).body(LoginResponse.from(result));
  }

  /**
   * 클라이언트 구글 로그인 1. 클라이언트에서 구글 인가코드 수령 2. 서버에 Header Authentication: Bearer {인가코드} 전송 3. 서버 JWT 토큰
   * 발급
   *
   * @param request : 구글 인가코드
   * @return : 로그인 응답
   */
  @PostMapping(path = "/google")
  public ResponseEntity<LoginResponse> googleLoginWithAuthCode(HttpServletRequest request) {
    LoginResult result =
        authService.loginWithClientAuthCode(
            GOOGLE, AuthHeaderUtil.getAuthorizationHeaderValue(request));

    return ResponseEntity.status(HttpStatus.OK).body(LoginResponse.from(result));
  }

  /**
   * 포스트맨 네이버 로그인 1. 포스트맨에서 네이버 토큰 수령 2. 서버에 Header Authentication : Bearer {토큰} 전송 3. 서버 JWT 토큰 발급
   *
   * @param request : 네이버 토큰
   * @return : 로그인 응답
   */
  @PostMapping(path = "/postman/naver")
  public ResponseEntity<LoginResponse> naverLoginWithAuthToken(HttpServletRequest request) {
    LoginResult result =
        authService.loginWithClientAuthToken(
            NAVER, AuthHeaderUtil.getAuthorizationHeaderValue(request));

    return ResponseEntity.status(HttpStatus.OK).body(LoginResponse.from(result));
  }

  /**
   * 클라이언트 네이버 로그인 1. 클라이언트에서 네이버 인가코드 수령 2. 서버에 Header Authentication: Bearer {인가코드} 전송 3. 서버 JWT
   * 토큰 발급
   *
   * @param request : 네이버 인가코드
   * @return : 로그인 응답
   */
  @PostMapping(path = "/naver")
  public ResponseEntity<LoginResponse> naverLoginWithAuthCode(HttpServletRequest request) {
    LoginResult result =
        authService.loginWithClientAuthCode(
            NAVER, AuthHeaderUtil.getAuthorizationHeaderValue(request));

    return ResponseEntity.status(HttpStatus.OK).body(LoginResponse.from(result));
  }

  /**
   * 서버 JWT 토큰 갱신
   *
   * @param request : 서버 JWT 토큰
   * @param loginId : 로그인 아이디
   * @return : 갱신된 JWT 토큰
   */
  @PutMapping(path = "/refresh")
  public ResponseEntity<RefreshResponse> refreshToken(
      HttpServletRequest request, @LoginId Long loginId) {
    RefreshResult result =
        authService.refreshAuthToken(AuthHeaderUtil.getAuthorizationHeaderValue(request), loginId);

    return ResponseEntity.status(HttpStatus.OK).body(RefreshResponse.from(result));
  }

  // TODO : 일반 유저 접근 방지 처리
  /**
   * 멤버 권한 상승
   *
   * @param request : 서버 JWT 토큰
   * @param loginId : 로그인 아이디
   * @return : 권한 상승 응답 (갱신된 JWT 토큰 포함)
   */
  @PatchMapping(path = "/promotion")
  public ResponseEntity<RefreshResponse> promote(
      HttpServletRequest request, @LoginId Long loginId) {
    RefreshResult result =
        authService.updateAuthToken(AuthHeaderUtil.getAuthorizationHeaderValue(request), loginId);

    return ResponseEntity.status(HttpStatus.OK).body(RefreshResponse.from(result));
  }

  /**
   * 로그아웃
   *
   * @param request : 서버 JWT 토큰
   * @return : 로그아웃 응답
   */
  @DeleteMapping("/logout")
  public ResponseEntity<LogoutResponse> logout(HttpServletRequest request) {
    LogoutResult result = authService.logout(AuthHeaderUtil.getAuthorizationHeaderValue(request));

    return ResponseEntity.status(HttpStatus.OK).body(LogoutResponse.from(result));
  }

  /**
   * 회원 탈퇴
   *
   * @param request : 서버 JWT 토큰
   * @param loginId : 회원 아이디
   * @return : 회원 탈퇴 응답
   */
  @DeleteMapping("/withdraw")
  public ResponseEntity<Void> withdraw(HttpServletRequest request, @LoginId Long loginId) {
    authService.withdraw(AuthHeaderUtil.getAuthorizationHeaderValue(request), loginId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
