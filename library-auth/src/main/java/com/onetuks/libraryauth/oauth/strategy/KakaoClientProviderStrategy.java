package com.onetuks.libraryauth.oauth.strategy;

import com.onetuks.libraryauth.config.KakaoClientConfig;
import com.onetuks.libraryauth.exception.TokenValidFailedException;
import com.onetuks.libraryauth.oauth.dto.KakaoAuthToken;
import com.onetuks.libraryauth.oauth.dto.KakaoUser;
import com.onetuks.librarydomain.member.model.vo.AuthInfo;
import com.onetuks.libraryobject.config.WebClientConfig;
import com.onetuks.libraryobject.enums.ClientProvider;
import com.onetuks.libraryobject.enums.RoleType;
import com.onetuks.libraryobject.error.ErrorCode;
import java.util.Objects;
import java.util.Set;
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
public class KakaoClientProviderStrategy implements ClientProviderStrategy {

  private final WebClient webClient;
  private final KakaoClientConfig kakaoClientConfig;

  public KakaoClientProviderStrategy(WebClient webClient, KakaoClientConfig kakaoClientConfig) {
    this.webClient = webClient;
    this.kakaoClientConfig = kakaoClientConfig;
  }

  @Override
  public AuthInfo getAuthInfo(String authToken) {
    KakaoUser kakaoUser =
        webClient
            .get()
            .uri(
                kakaoClientConfig
                    .kakaoClientRegistration()
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
            .bodyToMono(KakaoUser.class)
            .block();

    Objects.requireNonNull(kakaoUser);

    return AuthInfo.builder()
        .socialId(String.valueOf(kakaoUser.getId()))
        .clientProvider(ClientProvider.KAKAO)
        .roles(Set.of(RoleType.USER))
        .build();
  }

  @Override
  public KakaoAuthToken getOAuth2Token(String authCode) {
    return webClient
        .post()
        .uri(kakaoClientConfig.kakaoClientRegistration().getProviderDetails().getTokenUri())
        .headers(
            httpHeaders ->
                httpHeaders.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8"))
        .body(BodyInserters.fromFormData(buildFormData(authCode)))
        .retrieve()
        .onStatus(
            HttpStatusCode::is4xxClientError,
            clientResponse ->
                Mono.error(new TokenValidFailedException(ErrorCode.UNAUTHORIZED_TOKEN)))
        .onStatus(
            HttpStatusCode::is5xxServerError,
            clientResponse ->
                Mono.error(new TokenValidFailedException(ErrorCode.OAUTH_CLIENT_SERVER_ERROR)))
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
