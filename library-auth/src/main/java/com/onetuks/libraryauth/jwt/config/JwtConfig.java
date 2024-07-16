package com.onetuks.libraryauth.jwt.config;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

  @Getter
  @Value(value = "${jwt.issuer}")
  private String issuer;

  @Value(value = "${jwt.tokenSecretKey}")
  private String tokenSecretKey;

  @Getter
  @Value(value = "${jwt.accessTokenExpiryPeriod}")
  private long accessTokenExpiryPeriod;

  @Getter
  @Value(value = "${jwt.refreshTokenExpiryPeriod}")
  private long refreshTokenExpiryPeriod;

  public SecretKey getSecretKey() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(tokenSecretKey));
  }
}
