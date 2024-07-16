package com.onetuks.libraryauth.oauth.service;

import com.onetuks.libraryauth.jwt.util.AuthHeaderUtil;
import com.onetuks.libraryauth.oauth.handler.OAuth2ClientStrategyHandler;
import com.onetuks.libraryauth.oauth.strategy.dto.auth_token.ClientAuthToken;
import com.onetuks.libraryauth.oauth.strategy.dto.user_info.UserInfo;
import com.onetuks.libraryobject.enums.ClientProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OAuth2Service {

  private final OAuth2ClientStrategyHandler strategyHandler;

  public OAuth2Service(OAuth2ClientStrategyHandler oAuth2ClientStrategyHandler) {
    this.strategyHandler = oAuth2ClientStrategyHandler;
  }

  @Transactional
  public UserInfo getUserInfoWithClientAuthToken(ClientProvider clientProvider, String clientAuthToken) {
    return strategyHandler.getClientStrategy(clientProvider)
        .getUserInfo(clientAuthToken);
  }

  @Transactional
  public UserInfo getUserInfoWithClientAuthCode(ClientProvider clientProvider, String clientAuthCode) {
    ClientAuthToken clientAuthToken = strategyHandler.getClientStrategy(clientProvider)
        .getClientAuthToken(clientAuthCode);

    return this.getUserInfoWithClientAuthToken(
        clientProvider, AuthHeaderUtil.TOKEN_PREFIX + clientAuthToken.getAccessToken());
  }
}
