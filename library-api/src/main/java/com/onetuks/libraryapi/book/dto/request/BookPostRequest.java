package com.onetuks.libraryapi.book.dto.request;

import com.onetuks.librarydomain.book.service.dto.param.BookPostParam;
import com.onetuks.libraryobject.enums.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;
import org.hibernate.validator.constraints.Length;

public record BookPostRequest(
    @NotBlank @Length(min = 1) String title,
    @NotBlank @Length(min = 1, max = 20) String authorName,
    @Length(min = 13, max = 13) String isbn,
    @Length(min = 1, max = 30) String publisher,
    @Size(min = 1, max = 3) Set<Category> categories,
    @NotNull boolean isIndie,
    String coverImageFilename) {

  public BookPostParam to() {
    return new BookPostParam(
        title(),
        authorName(),
        isbn(),
        publisher(),
        categories(),
        isIndie(),
        coverImageFilename());
  }
}
