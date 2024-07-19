package com.onetuks.dbstorage.weekly.converter;

import com.onetuks.dbstorage.weekly.entity.WeeklyFeaturedBooksEventEntity;
import com.onetuks.librarydomain.weekly.model.WeeklyFeaturedBooksEvent;
import org.springframework.stereotype.Component;

@Component
public class WeeklyFeaturedBooksEventConverter {

  public WeeklyFeaturedBooksEventEntity toEntity(WeeklyFeaturedBooksEvent model) {
    return new WeeklyFeaturedBooksEventEntity(
        model.weeklyFeaturedBooksEventId(), model.startedAt(), model.endedAt());
  }

  public WeeklyFeaturedBooksEvent toModel(WeeklyFeaturedBooksEventEntity entity) {
    return new WeeklyFeaturedBooksEvent(
        entity.getWeeklyFeaturedBookEventId(), entity.getStartedAt(), entity.getEndedAt());
  }
}
