package com.onetuks.dbstorage.book.converter;

import com.onetuks.dbstorage.book.entity.BookEntity;
import com.onetuks.librarydomain.book.model.Book;
import com.onetuks.libraryobject.enums.ImageType;
import com.onetuks.libraryobject.vo.ImageFile;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class BookConverter {

  public BookEntity toEntity(Book book) {
    return new BookEntity(
        book.bookId(),
        book.title(),
        book.authorName(),
        book.introduction(),
        book.isbn(),
        book.publisher(),
        book.category(),
        Optional.ofNullable(book.coverImageFile())
            .map(ImageFile::fileName)
            .orElse(ImageFile.DEFAULT_COVER_IMAGE_URI),
        book.isIndie(),
        book.isPermitted());
  }

  public Book toDomain(BookEntity bookEntity) {
    return new Book(
        bookEntity.getBookId(),
        bookEntity.getTitle(),
        bookEntity.getAuthorName(),
        bookEntity.getIntroduction(),
        bookEntity.getIsbn(),
        bookEntity.getPublisher(),
        bookEntity.getCategory(),
        ImageFile.of(ImageType.COVER_IMAGE, bookEntity.getCoverImageUri()),
        bookEntity.getIsIndie(),
        bookEntity.getIsPermitted());
  }
}
