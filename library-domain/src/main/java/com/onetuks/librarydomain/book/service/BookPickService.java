package com.onetuks.librarydomain.book.service;

import com.onetuks.librarydomain.book.model.BookPick;
import com.onetuks.librarydomain.book.repository.BookPickRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookPickService {

  private final BookPickRepository bookPickRepository;

  public BookPickService(BookPickRepository bookPickRepository) {
    this.bookPickRepository = bookPickRepository;
  }

  @Transactional(readOnly = true)
  public Page<BookPick> searchMyBookPicks(long loginId, Pageable pageable) {
    return bookPickRepository.readAll(loginId, pageable);
  }
}
