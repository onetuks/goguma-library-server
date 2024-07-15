package com.onetuks.libraryauth.jwt;

import java.util.Optional;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class AuthTokenRedisRepository implements AuthTokenRepository {

  private final ValueOperations<String, String> authTokenValueOperations;

  public AuthTokenRedisRepository(StringRedisTemplate redisTemplate) {
    this.authTokenValueOperations = redisTemplate.opsForValue();
  }

  @Override
  public void save(String accessToken, String refreshToken) {
    authTokenValueOperations.set(accessToken, refreshToken);
  }

  @Override
  public void delete(String accessToken) {
    authTokenValueOperations.getOperations().delete(accessToken);
  }

  @Override
  public Optional<String> findRefreshToken(String accessToken) {
    String refreshToken = authTokenValueOperations.get(accessToken);
    return Optional.ofNullable(refreshToken);
  }
}
