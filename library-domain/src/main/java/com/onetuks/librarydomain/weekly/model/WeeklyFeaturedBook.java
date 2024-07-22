package com.onetuks.librarydomain.weekly.model;

import static com.onetuks.librarydomain.weekly.model.WeeklyFeaturedBooksEvent.getNextMondayMidnight;
import static com.onetuks.librarydomain.weekly.model.WeeklyFeaturedBooksEvent.getThisMondayMidnight;

import com.onetuks.librarydomain.book.model.Book;

public record WeeklyFeaturedBook(
    Long weeklyFeaturedBookId, WeeklyFeaturedBooksEvent weeklyFeaturedBooksEvent, Book book) {

  public static WeeklyFeaturedBook of(Book book) {
    return new WeeklyFeaturedBook(
        null,
        new WeeklyFeaturedBooksEvent(null, getThisMondayMidnight(), getNextMondayMidnight()),
        book);
  }
}
