package com.onetuks.librarytest;

import com.onetuks.libraryauth.controller.dto.LoginResponse;
import com.onetuks.libraryauth.service.dto.LoginResult;
import com.onetuks.libraryauth.util.OnlyForAdmin;
import com.onetuks.libraryobject.enums.RoleType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRestController {

  private final TestAuthService testAuthService;

  public TestRestController(TestAuthService testAuthService) {
    this.testAuthService = testAuthService;
  }

  /**
   * API 서버 동작 확인용 API
   *
   * @return : API 서버 동작 확인 메시지
   */
  @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> home() {
    return ResponseEntity.ok("Welcome to Goguma Library API");
  }

  /**
   * 테스트용 로그인 API
   *
   * <p>새로운 멤버 레코드 생성
   *
   * <p>새로운 멤버 JWT 토큰 발급
   *
   * @param roleType : 희망 권한
   * @return : 로그인 결과
   */
  @OnlyForAdmin
  @PostMapping(path = "/api/test/login", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<LoginResponse> login(
      @RequestParam(name = "role-type", required = false, defaultValue = "USER") RoleType roleType) {
    LoginResult result = testAuthService.login(roleType);
    LoginResponse response = LoginResponse.from(result);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
