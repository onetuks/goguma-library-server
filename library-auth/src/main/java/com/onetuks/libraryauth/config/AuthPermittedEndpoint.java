package com.onetuks.libraryauth.config;

public class AuthPermittedEndpoint {

  private AuthPermittedEndpoint() {}

  public static final String[] ENDPOINTS =
      new String[] {
        "/",
        "/health",
        "/error",
        "/docs/**",
        "/actuator/**",
        "/api/auth/login/**",
        "/api/test/login",
        "/api/books/recommend/weekly-featured",
        "/api/books/search",
        "/api/reviews"
      };
}
