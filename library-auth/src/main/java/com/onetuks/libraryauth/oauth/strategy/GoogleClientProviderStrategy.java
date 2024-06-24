package com.onetuks.libraryauth.oauth.strategy;

import com.onetuks.libraryauth.exception.TokenValidFailedException;
import com.onetuks.libraryauth.oauth.dto.GoogleUser;
import com.onetuks.libraryauth.oauth.dto.KakaoAuthToken;
import com.onetuks.libraryexternal.config.WebClientConfig;
import com.onetuks.librarydomain.member.model.vo.AuthInfo;
import com.onetuks.libraryobject.enums.ClientProvider;
import com.onetuks.libraryobject.enums.RoleType;
import com.onetuks.libraryobject.error.ErrorCode;
import java.util.List;
import java.util.Objects;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@ComponentScan(basePackageClasses = WebClientConfig.class)
public class GoogleClientProviderStrategy implements ClientProviderStrategy {

  private final WebClient webClient;

  public GoogleClientProviderStrategy(WebClient webClient) {
    this.webClient = webClient;
  }

  @Override
  public AuthInfo getAuthInfo(String authToken) {
    GoogleUser googleUser =
        webClient
            .get()
            .uri("https://www.googleapis.com/oauth2/v3/userinfo")
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
            .bodyToMono(GoogleUser.class)
            .block();

    Objects.requireNonNull(googleUser);

    return AuthInfo.builder()
        .socialId(googleUser.getSub())
        .clientProvider(ClientProvider.GOOGLE)
        .roles(List.of(RoleType.USER))
        .build();
  }

  @Override
  public KakaoAuthToken getOAuth2Token(String authCode) {
    return null;
  }
}
