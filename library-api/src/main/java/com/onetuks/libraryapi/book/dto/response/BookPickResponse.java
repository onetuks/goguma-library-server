package com.onetuks.libraryapi.book.dto.response;

import com.onetuks.librarydomain.book.model.BookPick;

public record BookPickResponse(long bookPickId, long memberId, long bookId) {

  public static BookPickResponse from(BookPick model) {
    if (model == null) {
      return null;
    }

    return new BookPickResponse(
        model.bookPickId(), model.member().memberId(), model.book().bookId());
  }
}
