package com.onetuks.libraryauth.jwt.service;

import com.onetuks.libraryauth.exception.TokenExpiredException;
import com.onetuks.libraryauth.exception.TokenIsLogoutException;
import com.onetuks.libraryauth.jwt.service.model.AuthToken;
import com.onetuks.libraryauth.jwt.repository.AuthTokenRepository;
import com.onetuks.libraryauth.jwt.service.provider.AuthTokenProvider;
import com.onetuks.libraryobject.enums.RoleType;
import com.onetuks.libraryobject.error.ErrorCode;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthTokenService {

  private final AuthTokenProvider authTokenProvider;
  private final AuthTokenRepository authTokenRepository;

  public AuthTokenService(AuthTokenProvider authTokenProvider, AuthTokenRepository authTokenRepository) {
    this.authTokenProvider = authTokenProvider;
    this.authTokenRepository = authTokenRepository;
  }

  @Transactional
  public AuthToken saveAccessToken(String socialId, long loginId, Set<RoleType> roleTypes) {
    AuthToken accessToken = authTokenProvider.provideAccessToken(socialId, loginId, roleTypes);
    AuthToken refreshToken = authTokenProvider.provideRefreshToken(socialId, loginId, roleTypes);

    authTokenRepository.save(accessToken.getToken(), refreshToken.getToken());

    return accessToken;
  }

  @Transactional
  public AuthToken refreshAccessToken(String accessTokenValue, long loginId) {
    AuthToken accessToken = authTokenProvider.convertToAuthToken(accessTokenValue);

    validateRefreshToken(accessToken);

    authTokenRepository.delete(accessToken.getToken());

    return saveAccessToken(accessToken.getSocialId(), loginId, accessToken.getRoleTypes());
  }

  @Transactional
  public AuthToken updateAccessToken(String accessTokenValue, long loginId, Set<RoleType> grantedRoles) {
    AuthToken accessToken = authTokenProvider.convertToAuthToken(accessTokenValue);

    authTokenRepository.delete(accessToken.getToken());

    return saveAccessToken(accessToken.getSocialId(), loginId, grantRoles(accessToken, grantedRoles));
  }

  @Transactional
  public boolean removeAccessToken(String accessTokenValue) {
    AuthToken accessToken = authTokenProvider.convertToAuthToken(accessTokenValue);

    authTokenRepository.delete(accessToken.getToken());

    return !authTokenRepository.existsById(accessToken.getToken());
  }

  @Transactional(readOnly = true)
  public AuthToken readAccessToken(String accessTokenValue) {
    AuthToken accessToken = authTokenProvider.convertToAuthToken(accessTokenValue);

    boolean isValidClaim = accessToken.isValidTokenClaims();
    boolean isNotExpired = authTokenRepository.existsById(accessToken.getToken());

    if (!isValidClaim || !isNotExpired) {
      throw new TokenIsLogoutException(ErrorCode.IS_LOGOUT_TOKEN);
    }

    return accessToken;
  }

  private void validateRefreshToken(AuthToken accessToken) {
    String refreshToken = authTokenRepository
        .findRefreshToken(accessToken.getToken())
        .orElseThrow(() -> new TokenExpiredException(ErrorCode.EXPIRED_REFRESH_TOKEN));
    AuthToken authToken = authTokenProvider.convertToAuthToken(refreshToken);

    boolean isValidRefreshToken = authToken.isValidTokenClaims();
    if (!isValidRefreshToken) {
      throw new TokenExpiredException(ErrorCode.EXPIRED_REFRESH_TOKEN);
    }
  }

  private Set<RoleType> grantRoles(AuthToken authToken, Set<RoleType> grantedRoles) {
    Set<RoleType> roles = ConcurrentHashMap.newKeySet();
    roles.addAll(authToken.getRoleTypes());
    roles.addAll(grantedRoles);
    return roles;
  }
}
