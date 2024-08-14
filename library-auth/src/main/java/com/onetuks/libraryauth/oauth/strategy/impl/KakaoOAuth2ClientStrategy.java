package com.onetuks.libraryauth.oauth.strategy.impl;

import com.onetuks.libraryauth.exception.TokenValidFailedException;
import com.onetuks.libraryauth.oauth.config.KakaoClientConfig;
import com.onetuks.libraryauth.oauth.strategy.OAuth2ClientStrategy;
import com.onetuks.libraryauth.oauth.strategy.dto.auth_token.KakaoAuthToken;
import com.onetuks.libraryauth.oauth.strategy.dto.user_info.KakaoUserInfo;
import com.onetuks.libraryauth.oauth.strategy.dto.user_info.UserInfo;
import com.onetuks.libraryobject.config.WebClientConfig;
import com.onetuks.libraryobject.enums.ClientProvider;
import com.onetuks.libraryobject.enums.RoleType;
import com.onetuks.libraryobject.error.ErrorCode;
import java.util.Objects;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@ComponentScan(basePackageClasses = WebClientConfig.class)
public class KakaoOAuth2ClientStrategy implements OAuth2ClientStrategy {

  private static final Logger log = LoggerFactory.getLogger(KakaoOAuth2ClientStrategy.class);
  private final WebClient webClient;
  private final KakaoClientConfig kakaoClientConfig;

  public KakaoOAuth2ClientStrategy(WebClient webClient, KakaoClientConfig kakaoClientConfig) {
    this.webClient = webClient;
    this.kakaoClientConfig = kakaoClientConfig;
  }

  @Override
  public UserInfo getUserInfo(String clientAuthToken) {
    KakaoUserInfo kakaoUserInfo =
        webClient
            .get()
            .uri(
                kakaoClientConfig
                    .kakaoClientRegistration()
                    .getProviderDetails()
                    .getUserInfoEndpoint()
                    .getUri())
            .headers(httpHeaders -> httpHeaders.set("Authorization", clientAuthToken))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                clientResponse ->
                    Mono.error(new TokenValidFailedException(ErrorCode.UNAUTHORIZED_TOKEN)))
            .onStatus(
                HttpStatusCode::is5xxServerError,
                clientResponse ->
                    Mono.error(new TokenValidFailedException(ErrorCode.OAUTH_CLIENT_SERVER_ERROR)))
            .bodyToMono(KakaoUserInfo.class)
            .block();

    Objects.requireNonNull(kakaoUserInfo);

    return UserInfo.builder()
        .socialId(String.valueOf(kakaoUserInfo.getId()))
        .clientProvider(ClientProvider.KAKAO)
        .roles(Set.of(RoleType.USER))
        .build();
  }

  @Override
  public KakaoAuthToken getClientAuthToken(String clientAuthCode) {
    return webClient
        .post()
        .uri(kakaoClientConfig.kakaoClientRegistration().getProviderDetails().getTokenUri())
        .headers(
            httpHeaders ->
                httpHeaders.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8"))
        .body(BodyInserters.fromFormData(buildFormData(clientAuthCode)))
        .retrieve()
        .onStatus(
            HttpStatusCode::is4xxClientError,
            clientResponse ->
                clientResponse.bodyToMono(String.class)
                    .flatMap(errorBody -> {
                      log.warn("카카오 토큰 요청 실패 - errorBody: {}", errorBody);
                      return Mono.error(
                          new TokenValidFailedException(ErrorCode.UNAUTHORIZED_TOKEN));
                    }))
        .onStatus(
            HttpStatusCode::is5xxServerError,
            clientResponse ->
                Mono.error(
                    new TokenValidFailedException(ErrorCode.OAUTH_CLIENT_SERVER_ERROR)))
        .bodyToMono(KakaoAuthToken.class)
        .block();
  }

  private MultiValueMap<String, String> buildFormData(String authToken) {
    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add("grant_type", "authorization_code");
    formData.add("client_id", kakaoClientConfig.getClientId());
    formData.add("client_secret", kakaoClientConfig.getClientSecret());
    formData.add("redirect_uri", kakaoClientConfig.kakaoClientRegistration().getRedirectUri());
    formData.add("code", authToken);
    return formData;
  }
}
