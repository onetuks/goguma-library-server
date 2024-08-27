package com.onetuks.librarypoint.config;

import com.onetuks.librarypoint.repository.entity.PointHistoryEntity;
import com.onetuks.librarypoint.repository.impl.PointHistoryEntityJpaRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = "com.onetuks.librarypoint")
@EntityScan(basePackageClasses = PointHistoryEntity.class)
@EnableJpaRepositories(basePackageClasses = PointHistoryEntityJpaRepository.class)
public class PointJpaConfig {}
