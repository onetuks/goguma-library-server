package com.onetuks.librarydomain;

import com.onetuks.librarydomain.book.model.Book;
import com.onetuks.librarydomain.book.model.BookPick;
import com.onetuks.librarydomain.member.model.Member;

public class BookPickFixture {

  public static BookPick create(Long bookPickId, Member member, Book book) {
    return new BookPick(bookPickId, member, book);
  }
}
