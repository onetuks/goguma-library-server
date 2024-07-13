package com.onetuks.libraryauth.oauth.strategy;

import com.onetuks.libraryauth.exception.TokenValidFailedException;
import com.onetuks.libraryauth.oauth.dto.KakaoAuthToken;
import com.onetuks.libraryauth.oauth.dto.NaverUser;
import com.onetuks.librarydomain.member.model.vo.AuthInfo;
import com.onetuks.libraryexternal.config.WebClientConfig;
import com.onetuks.libraryobject.enums.ClientProvider;
import com.onetuks.libraryobject.enums.RoleType;
import com.onetuks.libraryobject.error.ErrorCode;
import java.util.Objects;
import java.util.Set;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@ComponentScan(basePackageClasses = WebClientConfig.class)
public class NaverClientProviderStrategy implements ClientProviderStrategy {

  private final WebClient webClient;

  public NaverClientProviderStrategy(WebClient webClient) {
    this.webClient = webClient;
  }

  @Override
  public AuthInfo getAuthInfo(String authToken) {
    NaverUser naverUser =
        webClient
            .get()
            .uri("https://openapi.naver.com/v1/nid/me")
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
  public KakaoAuthToken getOAuth2Token(String authCode) {
    return null;
  }
}
