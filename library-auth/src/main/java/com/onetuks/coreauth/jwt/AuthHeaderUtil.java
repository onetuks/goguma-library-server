package com.onetuks.coreauth.jwt;

import jakarta.servlet.http.HttpServletRequest;

public class AuthHeaderUtil {

  public static final String HEADER_AUTHORIZATION = "Authorization";
  public static final String TOKEN_PREFIX = "Bearer ";

  private AuthHeaderUtil() {}

  public static String extractAuthToken(HttpServletRequest request) {

    String httpHeaderAuthorizationString = request.getHeader(HEADER_AUTHORIZATION);

    if (httpHeaderAuthorizationString == null
        || !httpHeaderAuthorizationString.startsWith(TOKEN_PREFIX)) {
      return null;
    }

    return httpHeaderAuthorizationString.substring(TOKEN_PREFIX.length());
  }
}
