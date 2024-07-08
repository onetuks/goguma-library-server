package com.onetuks.librarydomain.book.repository;

import com.onetuks.librarydomain.book.model.BookPick;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface BookPickRepository {

  BookPick create(BookPick bookPick);

  boolean read(long memberId, long bookId);

  Page<BookPick> readAll(long loginId, Pageable pageable);

  void delete(long bookPickId);
}
