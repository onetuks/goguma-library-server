package com.onetuks.libraryauth.jwt.util;

import jakarta.servlet.http.HttpServletRequest;

public class AuthHeaderUtil {

  public static final String HEADER_AUTHORIZATION = "Authorization";
  public static final String TOKEN_PREFIX = "Bearer ";

  private AuthHeaderUtil() {}

  public static String getAuthorizationHeaderValue(HttpServletRequest request) {
    String httpHeaderAuthorizationString = request.getHeader(HEADER_AUTHORIZATION);

    if (httpHeaderAuthorizationString == null) {
      throw new IllegalArgumentException("Authorization header is not found.");
    }

    return httpHeaderAuthorizationString.startsWith(TOKEN_PREFIX)
        ? httpHeaderAuthorizationString.substring(TOKEN_PREFIX.length())
        : httpHeaderAuthorizationString.trim();
  }
}
