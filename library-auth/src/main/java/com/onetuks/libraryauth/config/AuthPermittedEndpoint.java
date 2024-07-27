package com.onetuks.libraryauth.config;

public class AuthPermittedEndpoint {

  private AuthPermittedEndpoint() {}

  public static final String[] ENDPOINTS =
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
