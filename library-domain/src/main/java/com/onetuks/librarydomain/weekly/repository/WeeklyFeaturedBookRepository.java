package com.onetuks.librarydomain.weekly.repository;

import com.onetuks.librarydomain.weekly.model.WeeklyFeaturedBook;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface WeeklyFeaturedBookRepository {

  WeeklyFeaturedBook create(WeeklyFeaturedBook weeklyFeaturedBook);

  Page<WeeklyFeaturedBook> readAll(Pageable pageable);

  List<WeeklyFeaturedBook> readAll();
}
