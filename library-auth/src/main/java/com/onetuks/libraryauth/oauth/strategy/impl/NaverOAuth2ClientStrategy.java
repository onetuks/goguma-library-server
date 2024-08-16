package com.onetuks.libraryauth.oauth.strategy.impl;

import com.onetuks.libraryauth.exception.TokenValidFailedException;
import com.onetuks.libraryauth.oauth.config.NaverClientConfig;
import com.onetuks.libraryauth.oauth.strategy.OAuth2ClientStrategy;
import com.onetuks.libraryauth.oauth.strategy.dto.auth_token.NaverAuthToken;
import com.onetuks.libraryauth.oauth.strategy.dto.user_info.NaverUserInfo;
import com.onetuks.libraryauth.oauth.strategy.dto.user_info.UserInfo;
import com.onetuks.libraryobject.config.WebClientConfig;
import com.onetuks.libraryobject.enums.ClientProvider;
import com.onetuks.libraryobject.enums.RoleType;
import com.onetuks.libraryobject.error.ErrorCode;
import com.onetuks.libraryobject.util.URIBuilder;
import java.util.Objects;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@ComponentScan(basePackageClasses = WebClientConfig.class)
public class NaverOAuth2ClientStrategy implements OAuth2ClientStrategy {

  private static final Logger log = LoggerFactory.getLogger(NaverOAuth2ClientStrategy.class);
  private final WebClient webClient;
  private final URIBuilder uriBuilder;
  private final NaverClientConfig naverClientConfig;

  public NaverOAuth2ClientStrategy(
      WebClient webClient, URIBuilder uriBuilder, NaverClientConfig naverClientConfig) {
    this.webClient = webClient;
    this.uriBuilder = uriBuilder;
    this.naverClientConfig = naverClientConfig;
  }

  @Override
  public UserInfo getUserInfo(String clientAuthToken) {
    NaverUserInfo naverUserInfo =
        webClient
            .get()
            .uri(
                naverClientConfig
                    .naverClientRegistration()
                    .getProviderDetails()
                    .getUserInfoEndpoint()
                    .getUri())
            .headers(httpHeaders -> httpHeaders.set("Authorization", clientAuthToken))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                clientResponse ->
                    clientResponse
                        .bodyToMono(String.class)
                        .flatMap(
                            errorBody -> {
                              log.warn("네이버 유저정보 요청 실패 - errorBody: {}", errorBody);
                              return Mono.error(
                                  new TokenValidFailedException(ErrorCode.UNAUTHORIZED_TOKEN));
                            }))
            .onStatus(
                HttpStatusCode::is5xxServerError,
                clientResponse ->
                    Mono.error(new TokenValidFailedException(ErrorCode.OAUTH_CLIENT_SERVER_ERROR)))
            .bodyToMono(NaverUserInfo.class)
            .block();

    Objects.requireNonNull(naverUserInfo);
    Objects.requireNonNull(naverUserInfo.getResponse());

    return UserInfo.builder()
        .socialId(naverUserInfo.getResponse().getId())
        .clientProvider(ClientProvider.NAVER)
        .roles(Set.of(RoleType.USER))
        .build();
  }

  @Override
  public NaverAuthToken getClientAuthToken(String clientAuthCode) {
    return webClient
        .post()
        .uri(
            builder ->
                uriBuilder.buildUri(
                    naverClientConfig.naverClientRegistration().getProviderDetails().getTokenUri(),
                    buildParamsMap(clientAuthCode)))
        .retrieve()
        .onStatus(
            HttpStatusCode::is4xxClientError,
            clientResponse ->
                clientResponse
                    .bodyToMono(String.class)
                    .flatMap(
                        errorBody -> {
                          log.warn("네이버 토큰 요청 실패 - errorBody: {}", errorBody);
                          return Mono.error(
                              new TokenValidFailedException(ErrorCode.UNAUTHORIZED_TOKEN));
                        }))
        .onStatus(
            HttpStatusCode::is5xxServerError,
            clientResponse ->
                Mono.error(new TokenValidFailedException(ErrorCode.OAUTH_CLIENT_SERVER_ERROR)))
        .bodyToMono(NaverAuthToken.class)
        .block();
  }

  private MultiValueMap<String, String> buildParamsMap(String authCode) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("grant_type", "authorization_code");
    params.add("client_id", naverClientConfig.getClientId());
    params.add("client_secret", naverClientConfig.getClientSecret());
    params.add("state", naverClientConfig.getClientSecret());
    params.add("code", authCode);
    return params;
  }
}
