package com.onetuks.libraryapi.book.dto.response;

import com.onetuks.librarydomain.book.model.Book;
import com.onetuks.libraryobject.enums.Category;
import java.util.Set;
import org.springframework.data.domain.Page;

public record BookResponse(
    long bookId,
    String title,
    String authorName,
    String introduction,
    String isbn,
    String publisher,
    Set<Category> categories,
    String coverImageUrl,
    boolean isIndie,
    boolean isPermitted) {

  public static BookResponse from(Book book) {
    return new BookResponse(
        book.bookId(),
        book.title(),
        book.authorName(),
        book.introduction(),
        book.isbn(),
        book.publisher(),
        book.categories(),
        book.coverImageFile().getUrl(),
        book.isIndie(),
        book.isPermitted());
  }

  public record BookResponses(Page<BookResponse> responses) {

    public static BookResponses from(Page<Book> books) {
      return new BookResponses(books.map(BookResponse::from));
    }
  }
}
