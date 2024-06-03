package com.onetuks.coreauth.jwt;

import com.onetuks.coreauth.service.AuthService;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final AuthTokenProvider tokenProvider;
  private final AuthService authService;

  public JwtAuthenticationFilter(AuthTokenProvider tokenProvider, AuthService authService) {
    this.tokenProvider = tokenProvider;
    this.authService = authService;
  }

  @Override
  protected void doFilterInternal(
      @Nonnull HttpServletRequest request,
      @Nonnull HttpServletResponse response,
      @Nonnull FilterChain filterChain)
      throws ServletException, IOException {

    String accessToken = AuthHeaderUtil.extractAuthToken(request);

    if (accessToken != null) {
      AuthToken authToken = tokenProvider.convertToAuthToken(accessToken);

      if (authToken.isValidTokenClaims() && !authService.isLogout(authToken.getToken())) {
        Authentication authentication = authToken.getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    }

    filterChain.doFilter(request, response);
  }
}
