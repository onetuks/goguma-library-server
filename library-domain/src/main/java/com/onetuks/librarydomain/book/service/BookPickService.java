package com.onetuks.librarydomain.book.service;

import com.onetuks.librarydomain.book.model.BookPick;
import com.onetuks.librarydomain.book.repository.BookPickRepository;
import com.onetuks.librarydomain.book.repository.BookRepository;
import com.onetuks.librarydomain.member.repository.MemberRepository;
import com.onetuks.libraryobject.enums.CacheName;
import com.onetuks.libraryobject.exception.ApiAccessDeniedException;
import org.springframework.cache.annotation.Cacheable;
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
        new BookPick(null, memberRepository.read(loginId), bookRepository.read(bookId)));
  }

  @Transactional
  public void remove(long loginId, long bookPickId) {
    Long actualMemberId = bookPickRepository.read(bookPickId).member().memberId();

    if (actualMemberId != loginId) {
      throw new ApiAccessDeniedException("해당 유저에게 권한이 없는 요청입니다.");
    }

    bookPickRepository.delete(bookPickId);
  }

  @Transactional(readOnly = true)
  public Page<BookPick> searchAll(long loginId, Pageable pageable) {
    return bookPickRepository.readAll(loginId, pageable);
  }

  @Cacheable(value = CacheName.BOOK_PICKS, key = "#loginId" + "-" + "#bookId")
  @Transactional(readOnly = true)
  public BookPick searchExistence(long loginId, long bookId) {
    return bookPickRepository.read(loginId, bookId);
  }
}
