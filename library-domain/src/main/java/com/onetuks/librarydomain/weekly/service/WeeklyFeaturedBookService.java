package com.onetuks.librarydomain.weekly.service;

import com.onetuks.librarydomain.book.model.Book;
import com.onetuks.librarydomain.book.repository.BookRepository;
import com.onetuks.librarydomain.weekly.model.WeeklyFeaturedBook;
import com.onetuks.librarydomain.weekly.repository.WeeklyFeaturedBookRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WeeklyFeaturedBookService {

  private static final Logger log = LoggerFactory.getLogger(WeeklyFeaturedBookService.class);

  private final WeeklyFeaturedBookRepository weeklyFeaturedBookRepository;
  private final BookRepository bookRepository;

  public WeeklyFeaturedBookService(
      WeeklyFeaturedBookRepository weeklyFeaturedBookRepository, BookRepository bookRepository) {
    this.weeklyFeaturedBookRepository = weeklyFeaturedBookRepository;
    this.bookRepository = bookRepository;
  }

  @Scheduled(cron = "0 0 0 * * MON")
  @Transactional
  public void registerAll() {
    List<WeeklyFeaturedBook> allWeeklyFeaturedBooks = weeklyFeaturedBookRepository.readAll();
    List<Book> featuredBooks =
        bookRepository.readAllNotIn(
            allWeeklyFeaturedBooks.stream().map(WeeklyFeaturedBook::book).toList());

    featuredBooks.forEach(book -> weeklyFeaturedBookRepository.create(WeeklyFeaturedBook.of(book)));

    log.info("월요일 자정이 되어, 금주도서가 선정되었습니다. : {}", featuredBooks.toArray());
  }

  @Transactional(readOnly = true)
  public Page<Book> searchAllForThisWeek() {
    return weeklyFeaturedBookRepository.readAllForThisWeek().map(WeeklyFeaturedBook::book);
  }
}
