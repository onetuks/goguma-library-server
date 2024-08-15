package com.onetuks.libraryauth.oauth.strategy.impl;

import com.onetuks.libraryauth.exception.TokenValidFailedException;
import com.onetuks.libraryauth.oauth.config.GoogleClientConfig;
import com.onetuks.libraryauth.oauth.strategy.OAuth2ClientStrategy;
import com.onetuks.libraryauth.oauth.strategy.dto.auth_token.GoogleAuthToken;
import com.onetuks.libraryauth.oauth.strategy.dto.user_info.GoogleUserInfo;
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
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@ComponentScan(basePackageClasses = WebClientConfig.class)
public class GoogleOAuth2ClientStrategy implements OAuth2ClientStrategy {

  private static final Logger log = LoggerFactory.getLogger(GoogleOAuth2ClientStrategy.class);
  private final WebClient webClient;
  private final URIBuilder uriBuilder;
  private final GoogleClientConfig googleClientConfig;

  public GoogleOAuth2ClientStrategy(
      WebClient webClient, URIBuilder uriBuilder, GoogleClientConfig googleClientConfig) {
    this.webClient = webClient;
    this.uriBuilder = uriBuilder;
    this.googleClientConfig = googleClientConfig;
  }

  @Override
  public UserInfo getUserInfo(String clientAuthToken) {
    GoogleUserInfo googleUserInfo =
        webClient
            .get()
            .uri(
                googleClientConfig
                    .googleClientRegistration()
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
                              log.warn("구글 토큰 요청 실패 - errorBody: {}", errorBody);
                              return Mono.error(
                                  new TokenValidFailedException(ErrorCode.UNAUTHORIZED_TOKEN));
                            }))
            .onStatus(
                HttpStatusCode::is5xxServerError,
                clientResponse ->
                    Mono.error(new TokenValidFailedException(
                        ErrorCode.OAUTH_CLIENT_SERVER_ERROR)))
            .bodyToMono(GoogleUserInfo.class)
            .block();

    Objects.requireNonNull(googleUserInfo);

    return UserInfo.builder()
        .socialId(googleUserInfo.getSub())
        .clientProvider(ClientProvider.GOOGLE)
        .roles(Set.of(RoleType.USER))
        .build();
  }

  @Override
  public GoogleAuthToken getClientAuthToken(String clientAuthCode) {
    return webClient
        .post()
        .uri(
            builder ->
                uriBuilder.buildUri(
                    googleClientConfig
                        .googleClientRegistration()
                        .getProviderDetails()
                        .getTokenUri(),
                    buildParamsMap()))
        .headers(
            httpHeaders ->
                httpHeaders.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8"))
        .body(BodyInserters.fromFormData(buildFormData(clientAuthCode)))
        .retrieve()
        .onStatus(
            HttpStatusCode::is4xxClientError,
            clientResponse ->
                Mono.error(new TokenValidFailedException(ErrorCode.UNAUTHORIZED_TOKEN)))
        .onStatus(
            HttpStatusCode::is5xxServerError,
            clientResponse ->
                Mono.error(new TokenValidFailedException(ErrorCode.OAUTH_CLIENT_SERVER_ERROR)))
        .bodyToMono(GoogleAuthToken.class)
        .block();
  }

  private MultiValueMap<String, String> buildParamsMap() {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("client_id", googleClientConfig.getClientId());
    params.add("client_secret", googleClientConfig.getClientSecret());
    return params;
  }

  private MultiValueMap<String, String> buildFormData(String authCode) {
    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add("grant_type", "authorization_code");
    formData.add("code", authCode);
    formData.add("redirect_uri", googleClientConfig.googleClientRegistration().getRedirectUri());
    formData.add("state", googleClientConfig.getClientSecret());
    return formData;
  }
}
