package com.onetuks.libraryauth.oauth.strategy.dto.auth_token;

public record GoogleAuthToken(
    String issued_at,
    String scope,
    String application_name,
    String refresh_token_issued_at,
    String status,
    String refresh_token_status,
    String api_product_list,
    String expires_in,
    //    String developer.email,
    String organization_id,
    String token_type,
    String refresh_token,
    String client_id,
    String access_token,
    String organization_name,
    String refresh_token_expires_in,
    String refresh_count)
    implements ClientAuthToken {

  @Override
  public String getAccessToken() {
    return access_token;
  }
}
