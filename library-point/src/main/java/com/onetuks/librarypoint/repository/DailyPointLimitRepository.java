package com.onetuks.librarypoint.repository;

import com.onetuks.librarypoint.repository.entity.DailyPointLimit;
import org.springframework.data.repository.CrudRepository;

public interface DailyPointLimitRepository extends CrudRepository<DailyPointLimit, Long> {

  int REVIEW_PICK_DAILY_LIMIT = 5;
  int DEFAULT_CREDIT_COUNT = 0;
  int INITIAL_CREDIT_COUNT = 1;

  default boolean isCreditable(long memberId) {
    return this.findById(memberId)
        .map(DailyPointLimit::getCreditCount).orElse(0)
        < REVIEW_PICK_DAILY_LIMIT;
  }

  default boolean isDebitable(long memberId) {
    return this.findById(memberId)
        .map(DailyPointLimit::getCreditCount).orElse(0)
        > DEFAULT_CREDIT_COUNT;
  }

  default void increaseCreditCount(long memberId) {
    this.findById(memberId)
        .ifPresentOrElse(
            dailyPointLimit -> this.save(dailyPointLimit.increaseCount()),
            () -> this.save(new DailyPointLimit(memberId, INITIAL_CREDIT_COUNT)));
  }

  default void decreaseCreditCount(long memberId) {
    this.findById(memberId)
        .ifPresentOrElse(
            dailyPointLimit -> this.save(dailyPointLimit.decreaseCount()),
            () -> this.save(new DailyPointLimit(memberId, DEFAULT_CREDIT_COUNT)));
  }
}
