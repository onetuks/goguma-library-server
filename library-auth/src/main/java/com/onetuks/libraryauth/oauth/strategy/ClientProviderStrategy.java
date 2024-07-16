package com.onetuks.libraryauth.oauth.strategy;

import com.onetuks.libraryauth.oauth.strategy.dto.auth_token.ClientAuthToken;
import com.onetuks.librarydomain.member.model.vo.AuthInfo;

public interface ClientProviderStrategy {

  AuthInfo getAuthInfo(String authToken);

  ClientAuthToken getOAuth2Token(String authCode);
}
