package com.onetuks.dbstorage.book.converter;

import static com.onetuks.libraryobject.enums.ImageType.COVER_IMAGE;

import com.onetuks.dbstorage.book.entity.BookEntity;
import com.onetuks.librarydomain.book.model.Book;
import com.onetuks.libraryobject.vo.ImageFile;
import org.springframework.stereotype.Component;

@Component
public class BookConverter {

  public BookEntity toEntity(Book model) {
    return new BookEntity(
        model.bookId(),
        model.title(),
        model.authorName(),
        model.introduction(),
        model.isbn(),
        model.publisher(),
        model.categories(),
        model.coverImageFile().fileName(),
        model.isIndie(),
        model.isPermitted());
  }

  public Book toModel(BookEntity entity) {
    return new Book(
        entity.getBookId(),
        entity.getTitle(),
        entity.getAuthorName(),
        entity.getIntroduction(),
        entity.getIsbn(),
        entity.getPublisher(),
        entity.getCategories(),
        ImageFile.of(COVER_IMAGE, entity.getCoverImageUri()),
        entity.getIsIndie(),
        entity.getIsPermitted());
  }
}
