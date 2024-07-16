package com.onetuks.libraryauth.config;

import com.onetuks.libraryauth.jwt.AuthTokenRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories(basePackageClasses = AuthTokenRepository.class)
public class AuthRedisConfig {}
