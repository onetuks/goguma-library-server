package com.onetuks.coreauth.oauth;

import com.onetuks.coreauth.oauth.strategy.ClientProviderStrategy;
import com.onetuks.coreauth.oauth.strategy.GoogleClientProviderStrategy;
import com.onetuks.coreauth.oauth.strategy.KakaoClientProviderStrategy;
import com.onetuks.coreauth.oauth.strategy.NaverClientProviderStrategy;
import com.onetuks.libraryobject.enums.ClientProvider;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class ClientProviderStrategyHandler {

  private final Map<ClientProvider, ClientProviderStrategy> strategies = new ConcurrentHashMap<>();

  public ClientProviderStrategyHandler(
      KakaoClientProviderStrategy kakaoClientStrategy,
      GoogleClientProviderStrategy googleClientStrategy,
      NaverClientProviderStrategy naverClientStrategy) {
    this.strategies.put(ClientProvider.KAKAO, kakaoClientStrategy);
    this.strategies.put(ClientProvider.GOOGLE, googleClientStrategy);
    this.strategies.put(ClientProvider.NAVER, naverClientStrategy);
  }

  public ClientProviderStrategy getClientStrategy(ClientProvider provider) {
    return strategies.get(provider);
  }
}
