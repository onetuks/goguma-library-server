package com.onetuks.coreauth.jwt;

import java.util.Optional;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class AuthTokenRedisRepository implements AuthTokenRepository {

  private final ValueOperations<String, String> valueOperations;

  public AuthTokenRedisRepository(StringRedisTemplate redisTemplate) {
    this.valueOperations = redisTemplate.opsForValue();
  }

  @Override
  public void save(String accessToken, String refreshToken) {
    valueOperations.set(accessToken, refreshToken);
  }

  @Override
  public void delete(String accessToken) {
    valueOperations.getOperations().delete(accessToken);
  }

  @Override
  public Optional<String> findRefreshToken(String accessToken) {
    String refreshToken = valueOperations.get(accessToken);
    return Optional.ofNullable(refreshToken);
  }
}
