package com.onetuks.libraryauth.jwt.service.provider;

import static com.onetuks.libraryauth.jwt.service.model.AuthToken.AUTHORITIES_KEY;
import static com.onetuks.libraryauth.jwt.service.model.AuthToken.LOGIN_ID_KEY;

import com.onetuks.libraryauth.jwt.config.JwtConfig;
import com.onetuks.libraryauth.jwt.service.model.AuthToken;
import com.onetuks.libraryobject.enums.RoleType;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import java.util.Date;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class AuthTokenProvider {

  private final JwtConfig jwtConfig;

  public AuthTokenProvider(JwtConfig jwtConfig) {
    this.jwtConfig = jwtConfig;
  }

  public AuthToken provideAccessToken(String socialId, Long loginId, Set<RoleType> roleTypes) {
    return new AuthToken(
        createToken(socialId, loginId, roleTypes,
            getExpiryDate(jwtConfig.getAccessTokenExpiryPeriod())),
        jwtConfig.getSecretKey());
  }

  public AuthToken provideRefreshToken(String socialId, Long loginId, Set<RoleType> roleTypes) {
    return new AuthToken(
        createToken(socialId, loginId, roleTypes,
            getExpiryDate(jwtConfig.getRefreshTokenExpiryPeriod())),
        jwtConfig.getSecretKey());
  }

  public AuthToken convertToAuthToken(String token) {
    return new AuthToken(token, jwtConfig.getSecretKey());
  }

  private Date getExpiryDate(long expiryPeriod) {
    return new Date(System.currentTimeMillis() + expiryPeriod);
  }

  private String createToken(String socialId, Long loginId, Set<RoleType> roleTypes, Date expiry) {
    return Jwts.builder()
        .subject(socialId)
        .claim(LOGIN_ID_KEY, loginId)
        .claim(AUTHORITIES_KEY, roleTypes.stream().map(RoleType::name).toArray(String[]::new))
        .signWith(jwtConfig.getSecretKey(), SIG.HS256)
        .expiration(expiry)
        .issuer(jwtConfig.getIssuer())
        .issuedAt(new Date(System.currentTimeMillis()))
        .compact();
  }
}
