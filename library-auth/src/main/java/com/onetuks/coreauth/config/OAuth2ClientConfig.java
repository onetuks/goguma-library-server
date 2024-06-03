package com.onetuks.coreauth.config;

import com.onetuks.coreweb.config.WebClientConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

@Configuration
@ComponentScan(basePackageClasses = WebClientConfig.class)
public class OAuth2ClientConfig {

  private final KakaoClientConfig kakaoClientConfig;
  private final GoogleClientConfig googleClientConfig;
  private final NaverClientConfig naverClientConfig;

  public OAuth2ClientConfig(
      KakaoClientConfig kakaoClientConfig,
      GoogleClientConfig googleClientConfig,
      NaverClientConfig naverClientConfig) {
    this.kakaoClientConfig = kakaoClientConfig;
    this.googleClientConfig = googleClientConfig;
    this.naverClientConfig = naverClientConfig;
  }

  @Bean
  public ClientRegistrationRepository clientRegistrationRepository() {
    return new InMemoryClientRegistrationRepository(
        kakaoClientConfig.kakaoClientRegistration(),
        googleClientConfig.googleClientRegistration(),
        naverClientConfig.naverClientRegistration());
  }
}
