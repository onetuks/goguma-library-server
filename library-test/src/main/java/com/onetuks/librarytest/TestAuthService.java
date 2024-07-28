package com.onetuks.librarytest;

import com.onetuks.libraryauth.jwt.service.AuthTokenService;
import com.onetuks.libraryauth.jwt.service.model.AuthToken;
import com.onetuks.libraryauth.service.dto.LoginResult;
import com.onetuks.librarydomain.MemberFixture;
import com.onetuks.librarydomain.member.model.vo.AuthInfo;
import com.onetuks.librarydomain.member.service.MemberService;
import com.onetuks.librarydomain.member.service.dto.result.MemberAuthResult;
import com.onetuks.libraryobject.enums.RoleType;
import org.springframework.stereotype.Service;

@Service
public class TestAuthService {

  private final AuthTokenService authTokenService;
  private final MemberService memberService;

  public TestAuthService(AuthTokenService authTokenService, MemberService memberService) {
    this.authTokenService = authTokenService;
    this.memberService = memberService;
  }

  public LoginResult login(RoleType roleType) {
    AuthInfo authInfo = MemberFixture.create(null, roleType).authInfo();
    MemberAuthResult member = memberService.registerIfNotExists(authInfo);
    AuthToken authToken =
        authTokenService.saveAccessToken(authInfo.socialId(), member.memberId(), member.roles());

    return LoginResult.of(
        authToken.getToken(), member.isNewMember(), member.memberId(), member.roles());
  }
}
