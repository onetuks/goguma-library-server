package com.onetuks.dbstorage.weakly;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "weakly_featured_books_events")
public class WeaklyFeaturedBooksEventEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "weakly_featured_books_event_id", nullable = false)
  private Long weaklyFeaturedBookEventId;

  @Column(name = "started_at", nullable = false)
  private LocalDateTime startedAt;

  @Column(name = "ended_at", nullable = false)
  private LocalDateTime endedAt;

  public WeaklyFeaturedBooksEventEntity(
      Long weaklyFeaturedBookEventId,
      LocalDateTime startedAt,
      LocalDateTime endedAt) {
    this.weaklyFeaturedBookEventId = weaklyFeaturedBookEventId;
    this.startedAt = startedAt;
    this.endedAt = endedAt;
  }
}
