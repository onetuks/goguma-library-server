package com.onetuks.libraryauth.service;

import com.onetuks.libraryauth.jwt.service.AuthTokenService;
import com.onetuks.libraryauth.jwt.service.model.AuthToken;
import com.onetuks.libraryauth.oauth.service.OAuth2Service;
import com.onetuks.libraryauth.oauth.strategy.dto.user_info.UserInfo;
import com.onetuks.libraryauth.service.dto.LoginResult;
import com.onetuks.libraryauth.service.dto.LogoutResult;
import com.onetuks.libraryauth.service.dto.RefreshResult;
import com.onetuks.librarydomain.member.service.MemberService;
import com.onetuks.librarydomain.member.service.dto.result.MemberAuthResult;
import com.onetuks.libraryobject.enums.ClientProvider;
import com.onetuks.libraryobject.enums.RoleType;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

  private final AuthTokenService authTokenService;
  private final OAuth2Service oAuth2Service;
  private final MemberService memberService;

  public AuthService(
      AuthTokenService authTokenService,
      OAuth2Service oAuth2Service,
      MemberService memberService) {
    this.authTokenService = authTokenService;
    this.oAuth2Service = oAuth2Service;
    this.memberService = memberService;
  }

  @Transactional
  public LoginResult loginWithClientAuthToken(ClientProvider clientProvider, String authorizationHeader) {
    UserInfo userInfo = oAuth2Service.getUserInfoWithClientAuthToken(clientProvider, authorizationHeader);
    MemberAuthResult savedMember = memberService.registerIfNotExists(userInfo.toDomain());
    AuthToken newAuthToken = authTokenService.saveAccessToken(
        userInfo.socialId(), savedMember.memberId(), savedMember.roles());

    return LoginResult.of(
        newAuthToken.getToken(),
        savedMember.isNewMember(),
        savedMember.memberId(),
        savedMember.roles());
  }

  @Transactional
  public LoginResult loginWithClientAuthCode(ClientProvider clientProvider, String authorizationHeader) {
    String clientAuthCode = authorizationHeader.replace("Bearer ", "");
    UserInfo userInfo = oAuth2Service.getUserInfoWithClientAuthCode(clientProvider, clientAuthCode);
    MemberAuthResult savedMember = memberService.registerIfNotExists(userInfo.toDomain());
    AuthToken newAuthToken = authTokenService.saveAccessToken(
        userInfo.socialId(), savedMember.memberId(), savedMember.roles());

    return LoginResult.of(
        newAuthToken.getToken(),
        savedMember.isNewMember(),
        savedMember.memberId(),
        savedMember.roles());
  }

  @Transactional
  public RefreshResult refreshAuthToken(String accessToken, long loginId) {
    AuthToken newAccessToken = authTokenService.refreshAccessToken(accessToken, loginId);

    return RefreshResult.of(newAccessToken.getToken(), loginId, newAccessToken.getRoleTypes());
  }

  @Transactional
  public RefreshResult updateAuthToken(String accessToken, long loginId) {
    AuthToken newAccessToken = authTokenService.updateAccessToken(accessToken, loginId, Set.of(RoleType.ADMIN));

    memberService.editAuthorities(loginId, newAccessToken.getRoleTypes());

    return RefreshResult.of(newAccessToken.getToken(), loginId, newAccessToken.getRoleTypes());
  }

  @Transactional
  public LogoutResult logout(String accessToken) {
    boolean removalSucceed = authTokenService.removeAccessToken(accessToken);

    return new LogoutResult(removalSucceed);
  }

  @Transactional
  public void withdraw(String accessToken, long loginId) {
    boolean tokenRemovalSucceed = authTokenService.removeAccessToken(accessToken);
    boolean memberRemovalSucceed = memberService.remove(loginId);

    if (tokenRemovalSucceed && memberRemovalSucceed) {
      throw new IllegalStateException("회원 탈퇴에 실패했습니다.");
    }
  }
}
