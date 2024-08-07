package com.onetuks.librarydomain.weekly.model;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

public record WeeklyFeaturedBooksEvent(
    Long weeklyFeaturedBooksEventId, LocalDateTime startedAt, LocalDateTime endedAt) {

  public static LocalDateTime getThisMondayMidnight() {
    return LocalDateTime.now()
        .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        .toLocalDate()
        .atStartOfDay();
  }

  public static LocalDateTime getNextMondayMidnight() {
    return LocalDateTime.now()
        .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
        .toLocalDate()
        .atStartOfDay();
  }
}
