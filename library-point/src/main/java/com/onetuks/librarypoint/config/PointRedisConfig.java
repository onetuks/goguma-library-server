package com.onetuks.librarypoint.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories(basePackages = "com.onetuks.librarypoint.repository")
public class PointRedisConfig {}
