package com.onetuks.libraryapi.book.dto.response;

import com.onetuks.librarydomain.book.handler.dto.IsbnResult;
import com.onetuks.libraryobject.enums.Category;
import java.util.List;

public record BookIsbnGetResponse(
    String title,
    String authorName,
    String publisher,
    String isbn,
    List<Category> category,
    String coverImageUrl) {

  public static BookIsbnGetResponse from(IsbnResult isbnResult) {
    return new BookIsbnGetResponse(
        isbnResult.title(),
        isbnResult.authorName(),
        isbnResult.publisher(),
        isbnResult.isbn(),
        Category.parseRemainCode(isbnResult.kdc()),
        isbnResult.coverImageUrl());
  }
}
