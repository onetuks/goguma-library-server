package com.onetuks.coreauth.service;

import com.onetuks.coreauth.exception.TokenExpiredException;
import com.onetuks.coreauth.jwt.AuthToken;
import com.onetuks.coreauth.jwt.AuthTokenProvider;
import com.onetuks.coreauth.jwt.AuthTokenRepository;
import com.onetuks.coreauth.service.dto.LogoutResult;
import com.onetuks.coreauth.service.dto.RefreshResult;
import com.onetuks.libraryobject.enums.RoleType;
import com.onetuks.libraryobject.error.ErrorCode;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

  private final AuthTokenProvider authTokenProvider;
  private final AuthTokenRepository authTokenRepository;

  public AuthService(AuthTokenProvider authTokenProvider, AuthTokenRepository authTokenRepository) {
    this.authTokenProvider = authTokenProvider;
    this.authTokenRepository = authTokenRepository;
  }

  @Transactional
  public AuthToken saveAccessToken(String socialId, Long loginId, List<RoleType> roleTypes) {
    AuthToken accessToken = authTokenProvider.provideAccessToken(socialId, loginId, roleTypes);
    AuthToken refreshToken = authTokenProvider.provideRefreshToken(socialId, loginId, roleTypes);

    authTokenRepository.save(accessToken.getToken(), refreshToken.getToken());

    return accessToken;
  }

  @Transactional
  public RefreshResult updateAccessToken(
      AuthToken accessToken, Long loginId, List<RoleType> roleTypes) {
    String socialId = accessToken.getSocialId();

    validateRefreshToken(accessToken.getToken());

    authTokenRepository.delete(accessToken.getToken());
    AuthToken newAccessToken = saveAccessToken(socialId, loginId, roleTypes);

    return RefreshResult.of(newAccessToken.getToken(), loginId);
  }

  @Transactional
  public LogoutResult logout(AuthToken authToken) {
    authTokenRepository.delete(authToken.getToken());
    return new LogoutResult(true);
  }

  @Transactional(readOnly = true)
  public boolean isLogout(String accessToken) {
    return authTokenRepository.findRefreshToken(accessToken).isEmpty();
  }

  private void validateRefreshToken(String accessToken) {
    boolean isValidRefreshToken = findRefreshToken(accessToken).isValidTokenClaims();

    if (!isValidRefreshToken) {
      throw new TokenExpiredException(ErrorCode.EXPIRED_REFRESH_TOKEN);
    }
  }

  private AuthToken findRefreshToken(String accessToken) {
    String refreshToken =
        authTokenRepository
            .findRefreshToken(accessToken)
            .orElseThrow(() -> new TokenExpiredException(ErrorCode.EXPIRED_REFRESH_TOKEN));

    return authTokenProvider.convertToAuthToken(refreshToken);
  }
}
