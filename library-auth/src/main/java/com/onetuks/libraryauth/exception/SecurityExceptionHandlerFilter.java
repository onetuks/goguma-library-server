package com.onetuks.libraryauth.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetuks.libraryobject.error.ErrorCode;
import com.onetuks.libraryobject.error.ErrorResponse;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class SecurityExceptionHandlerFilter extends OncePerRequestFilter {

  private static final String CONTENT_TYPE = "application/json";

  @Override
  protected void doFilterInternal(
      @Nonnull HttpServletRequest request,
      @Nonnull HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {
    try {
      filterChain.doFilter(request, response);
    } catch (TokenExpiredException e) {
      logger.warn("TokenExpiredException: {}", e);
      setErrorResponse(e.getErrorCode(), response);
    } catch (TokenValidFailedException e) {
      logger.warn("TokenValidFailedException: {}", e);
      setErrorResponse(e.getErrorCode(), response);
    } catch (TokenIsLogoutException e) {
      logger.warn("TokenIsLogoutException: {}", e);
      setErrorResponse(e.getErrorCode(), response);
    }
  }

  private void setErrorResponse(ErrorCode errorCode, HttpServletResponse response)
      throws IOException {
    ErrorResponse errorResponse = ErrorResponse.of(errorCode);
    ObjectMapper objectMapper = new ObjectMapper();
    String json = objectMapper.writeValueAsString(errorResponse);

    response.setContentType(CONTENT_TYPE);
    response.getWriter().write(json);
  }
}
