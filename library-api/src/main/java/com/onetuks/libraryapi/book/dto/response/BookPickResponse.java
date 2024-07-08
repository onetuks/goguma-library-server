package com.onetuks.libraryapi.book.dto.response;

import com.onetuks.librarydomain.book.model.BookPick;

public record BookPickResponse(
    long bookPickId,
    long memberId,
    long bookId
) {

  public static BookPickResponse from(BookPick bookPick) {
    return new BookPickResponse(
        bookPick.bookPickId(),
        bookPick.member().memberId(),
        bookPick.book().bookId()
    );
  }
}
