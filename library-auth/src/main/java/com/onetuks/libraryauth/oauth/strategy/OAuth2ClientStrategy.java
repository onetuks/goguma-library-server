package com.onetuks.libraryauth.oauth.strategy;

import com.onetuks.libraryauth.oauth.strategy.dto.auth_token.ClientAuthToken;
import com.onetuks.libraryauth.oauth.strategy.dto.user_info.UserInfo;

public interface OAuth2ClientStrategy {

  UserInfo getUserInfo(String clientAuthToken);

  ClientAuthToken getClientAuthToken(String clientAuthCode);
}
