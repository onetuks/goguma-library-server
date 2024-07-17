package com.onetuks.libraryauth.oauth.handler;

import com.onetuks.libraryauth.oauth.strategy.OAuth2ClientStrategy;
import com.onetuks.libraryauth.oauth.strategy.impl.GoogleOAuth2ClientStrategy;
import com.onetuks.libraryauth.oauth.strategy.impl.KakaoOAuth2ClientStrategy;
import com.onetuks.libraryauth.oauth.strategy.impl.NaverOAuth2ClientStrategy;
import com.onetuks.libraryobject.enums.ClientProvider;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class OAuth2ClientStrategyHandler {

  private final Map<ClientProvider, OAuth2ClientStrategy> strategies = new ConcurrentHashMap<>();

  public OAuth2ClientStrategyHandler(
      KakaoOAuth2ClientStrategy kakaoClientStrategy,
      GoogleOAuth2ClientStrategy googleClientStrategy,
      NaverOAuth2ClientStrategy naverClientStrategy) {
    this.strategies.put(ClientProvider.KAKAO, kakaoClientStrategy);
    this.strategies.put(ClientProvider.GOOGLE, googleClientStrategy);
    this.strategies.put(ClientProvider.NAVER, naverClientStrategy);
  }

  public OAuth2ClientStrategy getClientStrategy(ClientProvider provider) {
    return strategies.get(provider);
  }
}
