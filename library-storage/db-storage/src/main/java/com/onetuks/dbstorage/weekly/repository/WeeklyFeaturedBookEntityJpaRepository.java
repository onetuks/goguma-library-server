package com.onetuks.dbstorage.weekly.repository;

import com.onetuks.dbstorage.weekly.entity.WeeklyFeaturedBookEntity;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeeklyFeaturedBookEntityJpaRepository
    extends JpaRepository<WeeklyFeaturedBookEntity, Long> {

  Page<WeeklyFeaturedBookEntity> findAllByWeeklyFeaturedBooksEventEntityStartedAtAfter(
      LocalDateTime thisMondayMidnight, Pageable pageable);
}
