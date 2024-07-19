package com.onetuks.librarydomain.weekly.repository;

import com.onetuks.librarydomain.weekly.model.WeeklyFeaturedBook;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@Repository
public interface WeeklyFeaturedBookRepository {

  int WEEKLY_FEATURED_BOOKS_COUNT = 3;

  WeeklyFeaturedBook create(WeeklyFeaturedBook weeklyFeaturedBook);

  Page<WeeklyFeaturedBook> readAllForThisWeek();

  List<WeeklyFeaturedBook> readAll();
}
