package com.onetuks.dbstorage.weekly.repository;

import com.onetuks.dbstorage.weekly.converter.WeeklyFeaturedBookConverter;
import com.onetuks.librarydomain.weekly.model.WeeklyFeaturedBook;
import com.onetuks.librarydomain.weekly.model.WeeklyFeaturedBooksEvent;
import com.onetuks.librarydomain.weekly.repository.WeeklyFeaturedBookRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class WeeklyFeaturedBookEntityRepository implements WeeklyFeaturedBookRepository {

  private final WeeklyFeaturedBookEntityJpaRepository repository;
  private final WeeklyFeaturedBookConverter converter;

  public WeeklyFeaturedBookEntityRepository(
      WeeklyFeaturedBookEntityJpaRepository repository, WeeklyFeaturedBookConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  @Override
  public WeeklyFeaturedBook create(WeeklyFeaturedBook weeklyFeaturedBook) {
    return converter.toModel(repository.save(converter.toEntity(weeklyFeaturedBook)));
  }

  @Override
  public Page<WeeklyFeaturedBook> readAll(Pageable pageable) {
    LocalDateTime thisMondayMidnight = WeeklyFeaturedBooksEvent.getThisMondayMidnight();

    return repository
        .findAllByWeeklyFeaturedBooksEventEntityStartedAtAfter(thisMondayMidnight, pageable)
        .map(converter::toModel);
  }

  @Override
  public List<WeeklyFeaturedBook> readAll() {
    return repository.findAll().stream().map(converter::toModel).toList();
  }
}
