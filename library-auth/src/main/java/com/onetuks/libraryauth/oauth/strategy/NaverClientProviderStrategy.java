package com.onetuks.libraryauth.oauth.strategy;

import com.onetuks.libraryauth.config.NaverClientConfig;
import com.onetuks.libraryauth.exception.TokenValidFailedException;
import com.onetuks.libraryauth.oauth.dto.NaverAuthToken;
import com.onetuks.libraryauth.oauth.dto.NaverUser;
import com.onetuks.librarydomain.member.model.vo.AuthInfo;
import com.onetuks.libraryobject.config.WebClientConfig;
import com.onetuks.libraryobject.enums.ClientProvider;
import com.onetuks.libraryobject.enums.RoleType;
import com.onetuks.libraryobject.error.ErrorCode;
import com.onetuks.libraryobject.util.URIBuilder;
import java.util.Objects;
import java.util.Set;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@ComponentScan(basePackageClasses = WebClientConfig.class)
public class NaverClientProviderStrategy implements ClientProviderStrategy {

  private final WebClient webClient;
  private final URIBuilder uriBuilder;
  private final NaverClientConfig naverClientConfig;

  public NaverClientProviderStrategy(
      WebClient webClient, URIBuilder uriBuilder, NaverClientConfig naverClientConfig) {
    this.webClient = webClient;
    this.uriBuilder = uriBuilder;
    this.naverClientConfig = naverClientConfig;
  }

  @Override
  public AuthInfo getAuthInfo(String authToken) {
    NaverUser naverUser =
        webClient
            .get()
            .uri(
                naverClientConfig
                    .naverClientRegistration()
                    .getProviderDetails()
                    .getUserInfoEndpoint()
                    .getUri())
            .headers(httpHeaders -> httpHeaders.set("Authorization", authToken))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                clientResponse ->
                    Mono.error(new TokenValidFailedException(ErrorCode.UNAUTHORIZED_TOKEN)))
            .onStatus(
                HttpStatusCode::is5xxServerError,
                clientResponse ->
                    Mono.error(new TokenValidFailedException(ErrorCode.OAUTH_CLIENT_SERVER_ERROR)))
            .bodyToMono(NaverUser.class)
            .block();

    Objects.requireNonNull(naverUser);
    Objects.requireNonNull(naverUser.getResponse());

    return AuthInfo.builder()
        .socialId(naverUser.getResponse().getId())
        .clientProvider(ClientProvider.NAVER)
        .roles(Set.of(RoleType.USER))
        .build();
  }

  @Override
  public NaverAuthToken getOAuth2Token(String authCode) {
    return webClient
        .post()
        .uri(
            builder ->
                uriBuilder.buildUri(
                    naverClientConfig.naverClientRegistration().getProviderDetails().getTokenUri(),
                    buildParamsMap(authCode)))
        .retrieve()
        .onStatus(
            HttpStatusCode::is4xxClientError,
            clientResponse ->
                Mono.error(new TokenValidFailedException(ErrorCode.UNAUTHORIZED_TOKEN)))
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
