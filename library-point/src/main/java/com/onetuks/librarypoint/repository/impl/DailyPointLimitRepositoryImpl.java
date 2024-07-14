package com.onetuks.librarypoint.repository.impl;

import com.onetuks.librarypoint.repository.DailyPointLimitRepository;
import java.util.Optional;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DailyPointLimitRepositoryImpl implements
    DailyPointLimitRepository {

  private static final int REVIEW_PICK_DAILY_LIMIT = 5;

  private final RedisTemplate<Long, Integer> dailyPointLimitRedisTemplate;

  public DailyPointLimitRepositoryImpl(RedisTemplate<Long, Integer> dailyPointLimitRedisTemplate) {
    this.dailyPointLimitRedisTemplate = dailyPointLimitRedisTemplate;
  }

  @Override
  public void save(long memberId, int creditCount) {
    dailyPointLimitRedisTemplate.opsForValue()
        .set(memberId, Math.max(creditCount, REVIEW_PICK_DAILY_LIMIT));
  }

  @Override
  public int find(long memberId) {
    return Optional.ofNullable(dailyPointLimitRedisTemplate.opsForValue().get(memberId))
        .orElse(0);
  }

  @Override
  public boolean isCreditable(long memberId) {
    return this.find(memberId) < REVIEW_PICK_DAILY_LIMIT;
  }

  @Override
  public void delete(long memberId) {
    dailyPointLimitRedisTemplate.delete(memberId);
  }
}
