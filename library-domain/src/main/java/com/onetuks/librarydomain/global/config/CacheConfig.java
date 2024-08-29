package com.onetuks.librarydomain.global.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.onetuks.libraryobject.enums.CacheType;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile(value = "!test")
@EnableCaching
@Configuration
public class CacheConfig {

  @Bean
  public List<CaffeineCache> caffeineCaches() {
    return Arrays.stream(CacheType.values())
        .map(
            cacheType ->
                new CaffeineCache(
                    cacheType.getCacheName(),
                    Caffeine.newBuilder()
                        .recordStats()
                        .expireAfterWrite(cacheType.getExpirationAfterWrite(), TimeUnit.SECONDS)
                        .maximumSize(cacheType.getMaximumCacheSize())
                        .build()))
        .toList();
  }

  @Bean
  public CacheManager cacheManager() {
    SimpleCacheManager cacheManager = new SimpleCacheManager();
    cacheManager.setCaches(caffeineCaches());
    return cacheManager;
  }
}
