package com.onetuks.coreauth.config;

public class AuthPermittedEndpoint {

  private AuthPermittedEndpoint() {}

  protected static final String[] ENDPOINTS =
      new String[] {
        "/",
        "/auth/kakao",
        "/auth/google",
        "/auth/naver",
        "/auth/postman/kakao",
        "/auth/postman/google",
        "/auth/postman/naver",
        "/error",
        "/docs/**",
        "/actuator/**"
      };
}
