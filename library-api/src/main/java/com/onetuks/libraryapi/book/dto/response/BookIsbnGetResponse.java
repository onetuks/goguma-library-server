package com.onetuks.libraryapi.book.dto.response;

import com.onetuks.librarydomain.book.handler.dto.IsbnResult;
import com.onetuks.libraryobject.enums.Category;
import java.util.Set;

public record BookIsbnGetResponse(
    String title,
    String authorName,
    String introduction,
    String publisher,
    String isbn,
    Set<Category> categories,
    String coverImageUrl) {

  public static BookIsbnGetResponse from(IsbnResult isbnResult) {
    return new BookIsbnGetResponse(
        isbnResult.title(),
        isbnResult.authorName(),
        isbnResult.introduction(),
        isbnResult.publisher(),
        isbnResult.isbn(),
        Category.parseToCategory(isbnResult.kdc()),
        isbnResult.coverImageUrl());
  }
}
