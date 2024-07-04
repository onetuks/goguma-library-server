package com.onetuks.libraryauth.util;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan(basePackageClasses = OnlyForAdminInterceptor.class)
public class OnlyForAdminInterceptorConfig implements WebMvcConfigurer {

  private final OnlyForAdminInterceptor interceptor;

  public OnlyForAdminInterceptorConfig(OnlyForAdminInterceptor interceptor) {
    this.interceptor = interceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(interceptor);
  }
}
