package com.onetuks.librarydomain.book.service;

import com.onetuks.librarydomain.book.model.BookPick;
import com.onetuks.librarydomain.book.repository.BookPickRepository;
import com.onetuks.librarydomain.book.repository.BookRepository;
import com.onetuks.librarydomain.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookPickService {

  private final BookPickRepository bookPickRepository;
  private final MemberRepository memberRepository;
  private final BookRepository bookRepository;

  public BookPickService(
      BookPickRepository bookPickRepository,
      MemberRepository memberRepository,
      BookRepository bookRepository) {
    this.bookPickRepository = bookPickRepository;
    this.memberRepository = memberRepository;
    this.bookRepository = bookRepository;
  }

  @Transactional
  public BookPick register(long loginId, long bookId) {
    return bookPickRepository.create(
        new BookPick(null,
            memberRepository.read(loginId),
            bookRepository.read(bookId)));
  }

  @Transactional(readOnly = true)
  public Page<BookPick> searchMyBookPicks(long loginId, Pageable pageable) {
    return bookPickRepository.readAll(loginId, pageable);
  }

  @Transactional(readOnly = true)
  public boolean searchIsMyBookPick(long loginId, long bookPickId) {
    return bookPickRepository.read(loginId, bookPickId);
  }
}
