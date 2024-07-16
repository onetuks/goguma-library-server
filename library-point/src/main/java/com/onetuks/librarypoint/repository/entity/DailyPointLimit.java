package com.onetuks.librarypoint.repository.entity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "daily_point_limit", timeToLive = 60 * 60 * 24)
public class DailyPointLimit {

  @Id private Long memberId;

  private Integer creditCount;

  @TimeToLive private Long expirationInSeconds;

  public DailyPointLimit(Long memberId, Integer creditCount) {
    this.memberId = memberId;
    this.creditCount = creditCount;
    this.expirationInSeconds = getTodayRemainSeconds();
  }

  private Long getTodayRemainSeconds() {
    return Duration.between(
            LocalDateTime.now(),
            LocalDateTime.of(LocalDateTime.now().toLocalDate().plusDays(1), LocalTime.MIDNIGHT))
        .getSeconds();
  }

  public DailyPointLimit increaseCount() {
    this.creditCount += 1;
    return this;
  }

  public DailyPointLimit decreaseCount() {
    this.creditCount = Math.max(creditCount - 1, 0);
    return this;
  }
}
