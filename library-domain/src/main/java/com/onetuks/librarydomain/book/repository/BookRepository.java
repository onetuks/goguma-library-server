package com.onetuks.librarydomain.book.repository;

import com.onetuks.librarydomain.book.model.Book;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository {

  Book create(Book book);

  Book read(long bookId);

  Book update(Book book);

  void delete(long bookId);
}
