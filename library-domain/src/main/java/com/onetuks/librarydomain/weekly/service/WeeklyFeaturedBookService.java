package com.onetuks.librarydomain.weekly.service;

import com.onetuks.librarydomain.book.model.Book;
import com.onetuks.librarydomain.book.repository.BookRepository;
import com.onetuks.librarydomain.weekly.model.WeeklyFeaturedBook;
import com.onetuks.librarydomain.weekly.repository.WeeklyFeaturedBookRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WeeklyFeaturedBookService {

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
            allWeeklyFeaturedBooks.stream().map(WeeklyFeaturedBook::book).toList(),
            PageRequest.of(0, 3));

    featuredBooks.forEach(book -> weeklyFeaturedBookRepository.create(WeeklyFeaturedBook.of(book)));
  }

  @Transactional(readOnly = true)
  public Page<Book> searchAll(Pageable pageable) {
    return weeklyFeaturedBookRepository.readAll(pageable).map(WeeklyFeaturedBook::book);
  }
}
