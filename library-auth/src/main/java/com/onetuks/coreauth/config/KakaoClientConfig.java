package com.onetuks.coreauth.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

@Getter
@Configuration
public class KakaoClientConfig {

  @Value("${oauth.kakao.client-id}")
  private String clientId;

  @Value("${oauth.kakao.client-secret}")
  private String clientSecret;

  public ClientRegistration kakaoClientRegistration() {
    return ClientRegistration.withRegistrationId("kakao")
        .clientId(clientId)
        .clientSecret(clientSecret)
        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
        .redirectUri("http://localhost:8000/login/oauth2/callback")
        .authorizationUri("https://kauth.kakao.com/oauth/authorize")
        .tokenUri("https://kauth.kakao.com/oauth/token")
        .userInfoUri("https://kapi.kakao.com/v2/user/me")
        .userNameAttributeName("id")
        .clientName("Kakao")
        .build();
  }
}
