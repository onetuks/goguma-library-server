package com.onetuks.librarydomain.weekly.model;

import com.onetuks.librarydomain.book.model.Book;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

public record WeeklyFeaturedBook(
    Long weeklyFeaturedBookId, WeeklyFeaturedBooksEvent weeklyFeaturedBooksEvent, Book book) {

  public static WeeklyFeaturedBook of(Book book) {
    return new WeeklyFeaturedBook(
        null,
        new WeeklyFeaturedBooksEvent(
            null,
            LocalDateTime.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)),
            LocalDateTime.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))),
        book);
  }
}
