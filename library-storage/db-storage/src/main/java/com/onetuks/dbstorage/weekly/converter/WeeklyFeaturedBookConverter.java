package com.onetuks.dbstorage.weekly.converter;

import com.onetuks.dbstorage.book.converter.BookConverter;
import com.onetuks.dbstorage.weekly.entity.WeeklyFeaturedBookEntity;
import com.onetuks.librarydomain.weekly.model.WeeklyFeaturedBook;
import org.springframework.stereotype.Component;

@Component
public class WeeklyFeaturedBookConverter {

  private final WeeklyFeaturedBooksEventConverter weeklyFeaturedBooksEventConverter;
  private final BookConverter bookConverter;

  public WeeklyFeaturedBookConverter(
      WeeklyFeaturedBooksEventConverter weeklyFeaturedBooksEventConverter,
      BookConverter bookConverter) {
    this.weeklyFeaturedBooksEventConverter = weeklyFeaturedBooksEventConverter;
    this.bookConverter = bookConverter;
  }

  public WeeklyFeaturedBookEntity toEntity(WeeklyFeaturedBook model) {
    return new WeeklyFeaturedBookEntity(
        model.weeklyFeaturedBookId(),
        weeklyFeaturedBooksEventConverter.toEntity(model.weeklyFeaturedBooksEvent()),
        bookConverter.toEntity(model.book()));
  }

  public WeeklyFeaturedBook toModel(WeeklyFeaturedBookEntity entity) {
    return new WeeklyFeaturedBook(
        entity.getWeeklyFeaturedBookId(),
        weeklyFeaturedBooksEventConverter.toModel(entity.getWeeklyFeaturedBooksEventEntity()),
        bookConverter.toModel(entity.getBook()));
  }
}
