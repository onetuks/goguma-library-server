package com.onetuks.libraryauth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.onetuks.libraryauth.CoreAuthIntegrationTest;
import com.onetuks.libraryauth.service.dto.LoginResult;
import com.onetuks.librarydomain.member.model.vo.AuthInfo;
import com.onetuks.librarydomain.member.service.dto.result.MemberAuthResult;
import com.onetuks.libraryobject.enums.ClientProvider;
import com.onetuks.libraryobject.enums.RoleType;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OAuth2ClientServiceTest extends CoreAuthIntegrationTest {

  @Test
  @DisplayName("구글 소셜 로그인 클라이언트를 활용해 로그인한다.")
  void login_WithAuthToken_GoogleClient_Test() {
    // Given
    ClientProvider clientProvider = ClientProvider.GOOGLE;
    AuthInfo authInfo = new AuthInfo("socialId", clientProvider, Set.of(RoleType.USER));
    MemberAuthResult memberAuthResult = new MemberAuthResult(true, 1L, authInfo.roles());

    given(googleClientProviderStrategy.getAuthInfo(anyString())).willReturn(authInfo);
    given(memberService.registerIfNotExists(authInfo)).willReturn(memberAuthResult);

    // When
    LoginResult result =
        oAuth2ClientService.loginWithAuthToken(clientProvider, "googleAccessToken");

    // Then
    assertAll(
        () -> assertThat(result.loginId()).isPositive(),
        () -> assertThat(result.roles()).containsExactly(RoleType.USER),
        () -> assertThat(result.isNewMember()).isTrue(),
        () -> assertThat(result.accessToken()).isInstanceOf(String.class));
  }
}
