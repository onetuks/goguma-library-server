package com.onetuks.dbstorage.weekly.repository;

import static com.onetuks.libraryobject.enums.CacheName.FEATURED_BOOKS_CACHE_KEY;

import com.onetuks.dbstorage.weekly.converter.WeeklyFeaturedBookConverter;
import com.onetuks.librarydomain.weekly.model.WeeklyFeaturedBook;
import com.onetuks.librarydomain.weekly.model.WeeklyFeaturedBooksEvent;
import com.onetuks.librarydomain.weekly.repository.WeeklyFeaturedBookRepository;
import com.onetuks.libraryobject.enums.CacheName;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
  public Page<WeeklyFeaturedBook> readAllForThisWeek() {
    LocalDateTime thisMondayMidnight = WeeklyFeaturedBooksEvent.getThisMondayMidnight();

    return repository
        .findAllByWeeklyFeaturedBooksEventEntityStartedAtGreaterThanEqual(
            thisMondayMidnight, PageRequest.of(0, WEEKLY_FEATURED_BOOKS_COUNT))
        .map(converter::toModel);
  }

  @Override
  @Cacheable(value = CacheName.WEEKLY_FEATURED_BOOKS, key = FEATURED_BOOKS_CACHE_KEY)
  public List<WeeklyFeaturedBook> readAll() {
    return repository.findAll().stream().map(converter::toModel).toList();
  }
}
