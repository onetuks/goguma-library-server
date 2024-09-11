package com.onetuks.librarypoint.config;

import com.onetuks.librarypoint.repository.DailyPointLimitRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories(basePackageClasses = DailyPointLimitRepository.class)
public class PointRedisConfig {}
