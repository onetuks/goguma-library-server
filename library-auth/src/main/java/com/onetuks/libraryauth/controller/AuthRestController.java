package com.onetuks.libraryauth.controller;

import static com.onetuks.libraryauth.jwt.AuthHeaderUtil.HEADER_AUTHORIZATION;
import static com.onetuks.libraryobject.enums.ClientProvider.GOOGLE;
import static com.onetuks.libraryobject.enums.ClientProvider.KAKAO;
import static com.onetuks.libraryobject.enums.ClientProvider.NAVER;

import com.onetuks.libraryauth.controller.dto.LoginResponse;
import com.onetuks.libraryauth.controller.dto.LogoutResponse;
import com.onetuks.libraryauth.controller.dto.RefreshResponse;
import com.onetuks.libraryauth.jwt.AuthHeaderUtil;
import com.onetuks.libraryauth.jwt.AuthToken;
import com.onetuks.libraryauth.jwt.AuthTokenProvider;
import com.onetuks.libraryauth.service.AuthService;
import com.onetuks.libraryauth.service.OAuth2ClientService;
import com.onetuks.libraryauth.service.dto.LoginResult;
import com.onetuks.libraryauth.service.dto.LogoutResult;
import com.onetuks.libraryauth.service.dto.RefreshResult;
import com.onetuks.libraryauth.util.LoginId;
import com.onetuks.librarydomain.member.service.MemberService;
import com.onetuks.libraryobject.enums.RoleType;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
public class AuthRestController {

  private final AuthTokenProvider authTokenProvider;
  private final OAuth2ClientService oAuth2ClientService;
  private final AuthService authService;
  private final MemberService memberService;

  public AuthRestController(
      AuthTokenProvider authTokenProvider,
      OAuth2ClientService oAuth2ClientService,
      AuthService authService,
      MemberService memberService) {
    this.authTokenProvider = authTokenProvider;
    this.oAuth2ClientService = oAuth2ClientService;
    this.authService = authService;
    this.memberService = memberService;
  }

  @PostMapping(path = "/postman/kakao")
  public ResponseEntity<LoginResponse> kakaoLoginWithAuthToken(HttpServletRequest request) {
    LoginResult result =
        oAuth2ClientService.loginWithAuthToken(KAKAO, request.getHeader(HEADER_AUTHORIZATION));

    return ResponseEntity.status(HttpStatus.OK).body(LoginResponse.from(result));
  }

  @PostMapping(path = "/kakao")
  public ResponseEntity<LoginResponse> kakaoLoginWithAuthCode(HttpServletRequest request) {
    LoginResult result =
        oAuth2ClientService.loginWithAuthCode(KAKAO, request.getHeader(HEADER_AUTHORIZATION));

    return ResponseEntity.status(HttpStatus.OK).body(LoginResponse.from(result));
  }

  @PostMapping(path = "/postman/google")
  public ResponseEntity<LoginResponse> googleLoginWithAuthToken(HttpServletRequest request) {
    LoginResult result =
        oAuth2ClientService.loginWithAuthToken(GOOGLE, request.getHeader(HEADER_AUTHORIZATION));

    return ResponseEntity.status(HttpStatus.OK).body(LoginResponse.from(result));
  }

  @PostMapping(path = "/postman/naver")
  public ResponseEntity<LoginResponse> naverLoginWithAuthToken(HttpServletRequest request) {
    LoginResult result =
        oAuth2ClientService.loginWithAuthToken(NAVER, request.getHeader(HEADER_AUTHORIZATION));

    return ResponseEntity.status(HttpStatus.OK).body(LoginResponse.from(result));
  }

  @PutMapping(path = "/refresh")
  public ResponseEntity<RefreshResponse> refreshToken(
      HttpServletRequest request, @LoginId Long loginId) {
    AuthToken authToken = getAuthToken(request);

    RefreshResult result =
        authService.updateAccessToken(authToken, loginId, authToken.getRoleTypes());

    return ResponseEntity.status(HttpStatus.OK).body(RefreshResponse.from(result));
  }

  // TODO : 일반 유저 접근 방지 처리
  @PutMapping(path = "/promotion")
  public ResponseEntity<RefreshResponse> promoteMember(
      HttpServletRequest request, @LoginId Long loginId) {
    AuthToken authToken = getAuthToken(request);

    List<RoleType> newRoles =
        Collections.synchronizedList(new ArrayList<>(authToken.getRoleTypes()));
    newRoles.add(RoleType.ADMIN);

    memberService.editAuthorities(loginId, newRoles);

    RefreshResult result = authService.updateAccessToken(authToken, loginId, newRoles);

    return ResponseEntity.status(HttpStatus.OK).body(RefreshResponse.from(result));
  }

  @DeleteMapping("/logout")
  public ResponseEntity<LogoutResponse> logout(HttpServletRequest request) {
    AuthToken authToken = getAuthToken(request);

    LogoutResult result = authService.logout(authToken);

    return ResponseEntity.status(HttpStatus.OK).body(LogoutResponse.from(result));
  }

  @DeleteMapping("/withdraw")
  public ResponseEntity<Void> withdrawMember(HttpServletRequest request, @LoginId Long memberId) {
    AuthToken authToken = getAuthToken(request);

    authService.logout(authToken);
    memberService.remove(memberId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  private AuthToken getAuthToken(HttpServletRequest request) {
    String accessToken = AuthHeaderUtil.extractAuthToken(request);
    return authTokenProvider.convertToAuthToken(accessToken);
  }
}
