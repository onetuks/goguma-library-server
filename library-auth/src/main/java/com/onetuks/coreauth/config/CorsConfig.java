package com.onetuks.coreauth.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

  private static final List<String> ALLOWED_ORIGINS = List.of("http://localhost:8000", "*");

  private static final List<String> ALLOWED_METHODS =
      List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS");

  private static final List<String> ALLOWED_HEADERS = List.of("*");

  private static final List<String> EXPOSED_HEADERS = List.of("*");

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowCredentials(true);
    configuration.setAllowedOriginPatterns(ALLOWED_ORIGINS);
    configuration.setAllowedMethods(ALLOWED_METHODS);
    configuration.setAllowedHeaders(ALLOWED_HEADERS);
    configuration.setExposedHeaders(EXPOSED_HEADERS);

    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
