package com.onetuks.dbstorage.weekly.entity;

import com.onetuks.libraryobject.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "weekly_featured_books_events")
public class WeeklyFeaturedBooksEventEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "weekly_featured_books_event_id", nullable = false)
  private Long weeklyFeaturedBookEventId;

  @Column(name = "started_at", nullable = false)
  private LocalDateTime startedAt;

  @Column(name = "ended_at", nullable = false)
  private LocalDateTime endedAt;

  public WeeklyFeaturedBooksEventEntity(
      Long weeklyFeaturedBookEventId, LocalDateTime startedAt, LocalDateTime endedAt) {
    this.weeklyFeaturedBookEventId = weeklyFeaturedBookEventId;
    this.startedAt = startedAt;
    this.endedAt = endedAt;
  }

  @Override
  @Generated
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WeeklyFeaturedBooksEventEntity that = (WeeklyFeaturedBooksEventEntity) o;
    return Objects.equals(weeklyFeaturedBookEventId, that.weeklyFeaturedBookEventId);
  }

  @Override
  @Generated
  public int hashCode() {
    return Objects.hashCode(weeklyFeaturedBookEventId);
  }
}
