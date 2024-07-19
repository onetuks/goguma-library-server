package com.onetuks.librarydomain.book.repository;

import com.onetuks.librarydomain.book.model.Book;
import com.onetuks.libraryobject.enums.Category;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository {

  int INTERESTED_CATEGORIES_RECOMMEND_BOOKS_COUNT = 3;
  int WEEKLY_FEATURED_RECOMMEND_BOOKS_COUNT = 3;

  Book create(Book book);

  Book read(long bookId);

  Page<Book> readAll(boolean inspectionMode, Pageable pageable);

  Page<Book> readAll(String keyword, Pageable pageable);

  Page<Book> readAll(Set<Category> interestedCategories);

  List<Book> readAllNotIn(List<Book> allWeeklyFeaturedBooks);

  Book update(Book book);

  void delete(long bookId);
}
