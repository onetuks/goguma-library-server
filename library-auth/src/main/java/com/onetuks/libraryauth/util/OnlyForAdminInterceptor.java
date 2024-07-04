package com.onetuks.libraryauth.util;

import com.onetuks.libraryobject.enums.RoleType;
import com.onetuks.libraryobject.exception.ApiAccessDeniedException;
import io.micrometer.common.lang.NonNullApi;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class OnlyForAdminInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(
      @Nonnull HttpServletRequest request,
      @Nonnull HttpServletResponse response,
      @Nonnull Object handler) {
    if (handler instanceof HandlerMethod) {
      boolean hasMethodAnnotation = ((HandlerMethod) handler).hasMethodAnnotation(OnlyForAdmin.class);
      if (hasMethodAnnotation) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        boolean isAdmin = authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(authority -> RoleType.ADMIN.name().equals(authority));

        if (!isAdmin) {
          throw new ApiAccessDeniedException("관리자만 접근 가능합니다.");
        }

        return true;
      }
    }
    return true;
  }
}
