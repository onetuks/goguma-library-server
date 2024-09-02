package com.onetuks.libraryauth.config;

public class AuthPermittedEndpoint {

  private AuthPermittedEndpoint() {}

  public static final String[] ENDPOINTS =
      new String[] {
        "/", "/greeting", "/api/auth/login/**", "/api/test/login", "/error", "/docs/**", "/actuator/**"
      };
}
