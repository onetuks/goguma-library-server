package com.onetuks.libraryauth.service;

import static com.onetuks.libraryobject.enums.RoleType.ADMIN;
import static com.onetuks.libraryobject.enums.RoleType.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.onetuks.libraryauth.CoreAuthIntegrationTest;
import com.onetuks.libraryauth.jwt.service.model.AuthToken;
import com.onetuks.libraryobject.enums.RoleType;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthTokenServiceTest extends CoreAuthIntegrationTest {

  @Test
  @DisplayName("JWT 토큰을 저장한다.")
  void saveAccessTokenTest() {
    // Given
    String socialId = "socialId";
    long loginId = 1L;
    Set<RoleType> roleTypes = Set.of(ADMIN, USER);

    // When
    AuthToken authToken = authTokenService.saveAccessToken(socialId, loginId, roleTypes);

    // Then
    assertThat(authToken).isNotNull();
  }

  @Test
  @DisplayName("JWT 토큰을 갱신한다.")
  void updateAccessTokenTest() {
    // Given
    long loginId = 1L;
    Set<RoleType> roleTypes = Set.of(ADMIN, USER);
    AuthToken authToken = authTokenService.saveAccessToken("socialId", loginId, roleTypes);

    // When
    AuthToken result = authTokenService.refreshAccessToken(authToken.getToken(), loginId);

    // Then
    assertAll(
        () -> assertThat(result.getToken()).isNotBlank(),
        () -> assertThat(result.getRoleTypes()).containsExactlyInAnyOrderElementsOf(roleTypes));
  }

  @Test
  @DisplayName("JWT 토큰을 삭제한다.")
  void removeAccessTokenTest() {
    // Given
    AuthToken authToken = authTokenService.saveAccessToken("socialId", 1L, Set.of(ADMIN, USER));

    // When
    boolean result = authTokenService.removeAccessToken(authToken.getToken());

    // Then
    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("로그아웃하지 않았을때 토큰 정보를 가져온다.")
  void isRemoveAccessToken_NotLogOuted_Test() {
    // Given
    AuthToken authToken = authTokenService.saveAccessToken("socialId", 1L, Set.of(ADMIN, USER));

    // When
    AuthToken result = authTokenService.readAccessToken(authToken.getToken());

    // Then
    assertAll(
        () -> assertThat(result.getToken()).isNotBlank(),
        () ->
            assertThat(result.getRoleTypes())
                .containsExactlyInAnyOrderElementsOf(authToken.getRoleTypes()),
        () -> assertThat(result.getSocialId()).isEqualTo(authToken.getSocialId()));
  }
}
