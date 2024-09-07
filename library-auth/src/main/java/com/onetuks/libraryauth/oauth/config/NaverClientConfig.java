package com.onetuks.libraryauth.oauth.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

@Getter
@Configuration
public class NaverClientConfig {

  @Value("${oauth.naver.client-id}")
  private String clientId;

  @Value("${oauth.naver.client_secret}")
  private String clientSecret;

  @Value("${oauth.naver.redirect-uri}")
  private String redirectUri;

  public ClientRegistration naverClientRegistration() {
    return ClientRegistration.withRegistrationId("naver")
        .clientId(clientId)
        .clientSecret(clientSecret)
        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
        .redirectUri(redirectUri)
        .authorizationUri("https://nid.naver.com/oauth2.0/authorize")
        .tokenUri("https://nid.naver.com/oauth2.0/token")
        .userInfoUri("https://openapi.naver.com/v1/nid/me")
        .userNameAttributeName("id")
        .clientName("Naver")
        .build();
  }
}
