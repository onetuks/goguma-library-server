package com.onetuks.librarydomain.weekly.service;

import com.onetuks.librarydomain.book.model.Book;
import com.onetuks.librarydomain.book.repository.BookRepository;
import com.onetuks.librarydomain.weekly.model.WeeklyFeaturedBook;
import com.onetuks.librarydomain.weekly.repository.WeeklyFeaturedBookRepository;
import com.onetuks.libraryobject.enums.CacheName;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WeeklyFeaturedBookService {

  private static final Logger log = LoggerFactory.getLogger(WeeklyFeaturedBookService.class);
  private static final String FEATURED_BOOKS_CACHE_KEY = "'featured_books'";

  private final WeeklyFeaturedBookRepository weeklyFeaturedBookRepository;
  private final BookRepository bookRepository;

  public WeeklyFeaturedBookService(
      WeeklyFeaturedBookRepository weeklyFeaturedBookRepository, BookRepository bookRepository) {
    this.weeklyFeaturedBookRepository = weeklyFeaturedBookRepository;
    this.bookRepository = bookRepository;
  }

  @Caching(
      evict = @CacheEvict(value = CacheName.WEEKLY_FEATURED_BOOKS, key = FEATURED_BOOKS_CACHE_KEY),
      put = @CachePut(value = CacheName.WEEKLY_FEATURED_BOOKS, key = FEATURED_BOOKS_CACHE_KEY))
  @Scheduled(cron = "0 0 0 * * MON")
  @Transactional
  public void registerAll() {
    List<WeeklyFeaturedBook> allWeeklyFeaturedBooks = weeklyFeaturedBookRepository.readAll();
    List<Book> featuredBooks =
        bookRepository.readAllNotIn(
            allWeeklyFeaturedBooks.stream().map(WeeklyFeaturedBook::book).toList());

    featuredBooks.forEach(book -> weeklyFeaturedBookRepository.create(WeeklyFeaturedBook.of(book)));

    log.info("[도서] 금주도서 선정 - featuredBooks: {}", featuredBooks.toArray());
  }

  @Cacheable(value = CacheName.WEEKLY_FEATURED_BOOKS, key = FEATURED_BOOKS_CACHE_KEY)
  @Transactional(readOnly = true)
  public Page<Book> searchAllForThisWeek() {
    return weeklyFeaturedBookRepository.readAllForThisWeek().map(WeeklyFeaturedBook::book);
  }
}
