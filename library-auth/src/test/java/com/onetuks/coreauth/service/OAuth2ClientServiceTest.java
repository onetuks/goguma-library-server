package com.onetuks.coreauth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.onetuks.coreauth.CoreAuthIntegrationTest;
import com.onetuks.coreauth.oauth.strategy.GoogleClientProviderStrategy;
import com.onetuks.coreauth.service.dto.LoginResult;
import com.onetuks.librarydomain.member.model.vo.AuthInfo;
import com.onetuks.librarydomain.member.service.MemberService;
import com.onetuks.librarydomain.member.service.dto.result.MemberAuthResult;
import com.onetuks.libraryobject.enums.ClientProvider;
import com.onetuks.libraryobject.enums.RoleType;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class OAuth2ClientServiceTest extends CoreAuthIntegrationTest {

  @Autowired private OAuth2ClientService oAuth2ClientService;

  @MockBean private GoogleClientProviderStrategy googleClientProviderStrategy;
  @MockBean private MemberService memberService;

  @Test
  @DisplayName("구글 소셜 로그인 클라이언트를 활용해 로그인한다.")
  void login_WithAuthToken_GoogleClient_Test() {
    // Given
    ClientProvider clientProvider = ClientProvider.GOOGLE;
    AuthInfo authInfo = new AuthInfo("socialId", clientProvider, List.of(RoleType.USER));
    MemberAuthResult memberAuthResult =
        new MemberAuthResult(
            true,
            1L,
            authInfo.roles());

    given(googleClientProviderStrategy.getAuthInfo(anyString())).willReturn(authInfo);
    given(memberService.createMemberIfNotExists(authInfo)).willReturn(memberAuthResult);

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
