package com.onetuks.libraryauth.jwt.filter;

import com.onetuks.libraryauth.config.AuthPermittedEndpoint;
import com.onetuks.libraryauth.jwt.service.AuthTokenService;
import com.onetuks.libraryauth.jwt.service.model.AuthToken;
import com.onetuks.libraryauth.jwt.util.AuthHeaderUtil;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
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
    try {
      String accessToken = AuthHeaderUtil.getAuthorizationHeaderValue(request);
      AuthToken authToken = authTokenService.readAccessToken(accessToken);
      Authentication authentication = authToken.getAuthentication();

      SecurityContextHolder.getContext().setAuthentication(authentication);
    } catch (NullPointerException e) {
      boolean isAuthPermittedAccess =
          Arrays.stream(AuthPermittedEndpoint.ENDPOINTS)
              .anyMatch(endpoint -> endpoint.matches(request.getRequestURI()));

      if (isAuthPermittedAccess) {
        log.info("[인증] 비인증 API 요청입니다.");
      } else {
        log.info(
            "[인증] HTTP 요청에 Authentication 헤더가 비어있습니다 - URL: {} / Header: {}",
            request.getRequestURL(),
            request.getHeader(AuthHeaderUtil.HEADER_AUTHORIZATION));
      }
    } catch (MalformedJwtException e) {
      log.info(
          "[인증] JWT 토큰이 올바르지 않습니다. (소셜 로그인 인가코드/인증토큰으로 로그인 중일 수 있습니다.) - URL: {} / Header: {}",
          request.getRequestURL(),
          request.getHeader(AuthHeaderUtil.HEADER_AUTHORIZATION));
    } catch (RuntimeException e) {
      boolean isAuthPermittedAccess = Arrays.stream(AuthPermittedEndpoint.ENDPOINTS)
          .anyMatch(endpoint -> request.getRequestURL().toString().contains(endpoint));

      if (!isAuthPermittedAccess) {
        log.warn(
            "[인증] JWT 토큰 검증 중 오류가 발생했습니다. (비정상적인 접근일 가능성이 있습니다.) - URL: {} / Header: {} / Exception: {}",
            request.getRequestURL(),
            request.getHeader(AuthHeaderUtil.HEADER_AUTHORIZATION),
            e.getMessage());
      } else {
        log.info(
            "[인증] 비인증 API 요청입니다. - URL: {} / Header: {} / Exception: {}",
            request.getRequestURL(),
            request.getHeader(AuthHeaderUtil.HEADER_AUTHORIZATION),
            e.getMessage());
      }
    } finally {
      filterChain.doFilter(request, response);
    }
  }
}
