package com.onetuks.libraryauth.oauth.dto;

public record NaverAuthToken(
    String access_token,
    String refresh_token,
    String token_type,
    Integer expires_in,
    String error,
    String error_description) implements ClientAuthToken {

  @Override
  public String getAccessToken() {
    return access_token;
  }
}
