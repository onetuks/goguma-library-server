package com.onetuks.librarydomain.book.repository;

import com.onetuks.librarydomain.book.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository {

  Book create(Book book);

  Book read(long bookId);

  Page<Book> readAll(boolean inspectionMode, Pageable pageable);

  Book update(Book book);

  void delete(long bookId);
}
