package com.onetuks.libraryauth.jwt.service.model;

import com.onetuks.libraryauth.jwt.service.model.vo.CustomUserDetails;
import com.onetuks.libraryobject.enums.RoleType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Slf4j
public class AuthToken {

  public static final String AUTHORITIES_KEY = "roles";
  public static final String LOGIN_ID_KEY = "loginId";

  @Getter private final String token;
  private final SecretKey secretKey;

  public AuthToken(String token, SecretKey secretKey) {
    this.token = token;
    this.secretKey = secretKey;
  }

  public String getSocialId() {
    return getTokenClaims().getSubject();
  }

  public Set<RoleType> getRoleTypes() {
    List<?> roles = getTokenClaims().get(AUTHORITIES_KEY, List.class);
    return roles.stream().map(role -> RoleType.valueOf((String) role)).collect(Collectors.toSet());
  }

  public Authentication getAuthentication() {
    Claims claims = getTokenClaims();

    String socialId = claims.getSubject();
    Long loginId = claims.get(LOGIN_ID_KEY, Long.class);
    Set<RoleType> roles = getRoleTypes();

    List<SimpleGrantedAuthority> authorities =
        roles.stream().map(RoleType::name).map(SimpleGrantedAuthority::new).toList();

    CustomUserDetails customUserDetails =
        CustomUserDetails.builder()
            .socialId(socialId)
            .loginId(loginId)
            .authorities(authorities)
            .build();

    return new UsernamePasswordAuthenticationToken(customUserDetails, this, authorities);
  }

  public boolean isValidTokenClaims() {
    try {
      return Optional.ofNullable(getTokenClaims()).isPresent();
    } catch (SecurityException e) {
      log.info("Invalid JWT signature.");
      return false;
    } catch (MalformedJwtException e) {
      log.info("Invalid JWT token. {}", e.getCause().toString());
      return false;
    } catch (ExpiredJwtException e) {
      log.info("Expired JWT token.");
      return false;
    } catch (UnsupportedJwtException e) {
      log.info("Unsupported JWT token.");
      return false;
    } catch (IllegalArgumentException e) {
      log.info("JWT token compact of handler are invalid.");
      return false;
    } catch (Exception e) {
      log.info("Unexpected JWT token error.");
      return false;
    }
  }

  private Claims getTokenClaims() {
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
  }
}
