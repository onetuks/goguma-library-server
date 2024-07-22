package com.onetuks.libraryauth.jwt.filter;

import com.onetuks.libraryauth.jwt.service.AuthTokenService;
import com.onetuks.libraryauth.jwt.service.model.AuthToken;
import com.onetuks.libraryauth.jwt.util.AuthHeaderUtil;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

  private final AuthTokenService authTokenService;

  public JwtAuthenticationFilter(AuthTokenService authTokenService) {
    this.authTokenService = authTokenService;
  }

  @Override
  protected void doFilterInternal(
      @Nonnull HttpServletRequest request,
      @Nonnull HttpServletResponse response,
      @Nonnull FilterChain filterChain)
      throws ServletException, IOException {
    String accessToken = AuthHeaderUtil.getAuthorizationHeaderValue(request);
    try {
      AuthToken authToken = authTokenService.readAccessToken(accessToken);
      Authentication authentication = authToken.getAuthentication();

      SecurityContextHolder.getContext().setAuthentication(authentication);
    } catch (Exception e) {
      log.info("소셜 로그인 인가코드/인증토큰으로 로그인 중입니다. (토큰 자체가 유효하지 않은 경우엔 클라이언트 인증 과정에서 예외가 발생합니다)");
    } finally {
      filterChain.doFilter(request, response);
    }
  }
}
