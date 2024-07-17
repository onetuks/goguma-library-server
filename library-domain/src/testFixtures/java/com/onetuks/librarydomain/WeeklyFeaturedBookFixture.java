package com.onetuks.librarydomain;

import com.onetuks.librarydomain.book.model.Book;
import com.onetuks.librarydomain.weekly.model.WeeklyFeaturedBook;
import com.onetuks.librarydomain.weekly.model.WeeklyFeaturedBooksEvent;

public class WeeklyFeaturedBookFixture {

  public static WeeklyFeaturedBook create(Long weeklyFeaturedBookId, Book book) {
    return new WeeklyFeaturedBook(weeklyFeaturedBookId, createWeeklyFeaturedBooksEvent(), book);
  }

  private static WeeklyFeaturedBooksEvent createWeeklyFeaturedBooksEvent() {
    return new WeeklyFeaturedBooksEvent(
        null,
        WeeklyFeaturedBooksEvent.getThisMondayMidnight(),
        WeeklyFeaturedBooksEvent.getNextMondayMidnight());
  }
}
