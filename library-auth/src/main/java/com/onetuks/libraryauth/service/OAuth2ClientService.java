package com.onetuks.libraryauth.service;

import com.onetuks.libraryauth.jwt.AuthHeaderUtil;
import com.onetuks.libraryauth.jwt.AuthToken;
import com.onetuks.libraryauth.oauth.ClientProviderStrategyHandler;
import com.onetuks.libraryauth.oauth.dto.ClientAuthToken;
import com.onetuks.libraryauth.oauth.strategy.ClientProviderStrategy;
import com.onetuks.libraryauth.service.dto.LoginResult;
import com.onetuks.librarydomain.member.model.vo.AuthInfo;
import com.onetuks.librarydomain.member.service.MemberService;
import com.onetuks.librarydomain.member.service.dto.result.MemberAuthResult;
import com.onetuks.libraryobject.enums.ClientProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OAuth2ClientService {

  private final ClientProviderStrategyHandler clientProviderStrategyHandler;
  private final AuthService authService;
  private final MemberService memberService;

  public OAuth2ClientService(
      ClientProviderStrategyHandler clientProviderStrategyHandler,
      AuthService authService,
      MemberService memberService) {
    this.clientProviderStrategyHandler = clientProviderStrategyHandler;
    this.authService = authService;
    this.memberService = memberService;
  }

  @Transactional
  public LoginResult loginWithAuthToken(ClientProvider clientProvider, String authToken) {
    ClientProviderStrategy clientProviderStrategy =
        clientProviderStrategyHandler.getClientStrategy(clientProvider);

    AuthInfo clientMember = clientProviderStrategy.getAuthInfo(authToken);

    MemberAuthResult savedMember = memberService.registerIfNotExists(clientMember);

    AuthToken newAuthToken =
        authService.saveAccessToken(
            clientMember.socialId(), savedMember.memberId(), savedMember.roles());

    return LoginResult.of(
        newAuthToken.getToken(),
        savedMember.isNewMember(),
        savedMember.memberId(),
        savedMember.roles());
  }

  @Transactional
  public LoginResult loginWithAuthCode(ClientProvider clientProvider, String authCode) {
    ClientProviderStrategy clientProviderStrategy =
        clientProviderStrategyHandler.getClientStrategy(clientProvider);

    String coreCode = authCode.replace("Bearer ", "");
    ClientAuthToken authToken = clientProviderStrategy.getOAuth2Token(coreCode);

    return loginWithAuthToken(
        clientProvider, AuthHeaderUtil.TOKEN_PREFIX + authToken.getAccessToken());
  }
}
