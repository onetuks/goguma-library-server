package com.onetuks.libraryapi.book.dto.request;

import com.onetuks.librarydomain.book.service.dto.param.BookPostParam;
import com.onetuks.libraryobject.enums.Category;
import java.util.List;

public record BookPostRequest(
    String title,
    String authorName,
    String isbn,
    String publisher,
    List<Category> categories,
    boolean isIndie) {

  public BookPostParam to() {
    return new BookPostParam(title(), authorName(), isbn(), publisher(), categories(), isIndie());
  }
}
