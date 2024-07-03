package com.onetuks.libraryapi.book.dto.response;

import com.onetuks.librarydomain.book.model.Book;

public record BookResponse(long bookId) {

  public static BookResponse from(Book book) {
    return new BookResponse(book.bookId());
  }
}
