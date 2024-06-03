package com.onetuks.coreauth.oauth.strategy;

import com.onetuks.coreauth.oauth.dto.KakaoAuthToken;
import com.onetuks.librarydomain.member.model.vo.AuthInfo;

public interface ClientProviderStrategy {

  AuthInfo getAuthInfo(String authToken);

  KakaoAuthToken getOAuth2Token(String authCode);
}
