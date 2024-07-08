package com.onetuks.dbstorage.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = "com.onetuks.dbstorage")
@EnableAutoConfiguration
@EntityScan(basePackages = "com.onetuks.dbstorage")
@EnableJpaRepositories(basePackages = "com.onetuks.dbstorage")
public class JpaConfig {}
