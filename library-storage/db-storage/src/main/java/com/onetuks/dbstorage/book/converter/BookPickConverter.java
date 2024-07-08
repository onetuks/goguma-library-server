package com.onetuks.dbstorage.book.converter;

import com.onetuks.dbstorage.book.entity.BookPickEntity;
import com.onetuks.dbstorage.member.converter.MemberConverter;
import com.onetuks.librarydomain.book.model.BookPick;
import org.springframework.stereotype.Component;

@Component
public class BookPickConverter {

  private final MemberConverter memberConverter;
  private final BookConverter bookConverter;

  public BookPickConverter(MemberConverter memberConverter, BookConverter bookConverter) {
    this.memberConverter = memberConverter;
    this.bookConverter = bookConverter;
  }

  public BookPickEntity toEntity(BookPick bookPick) {
    return new BookPickEntity(
        bookPick.bookPickId(),
        memberConverter.toEntity(bookPick.member()),
        bookConverter.toEntity(bookPick.book()));
  }

  public BookPick toDomain(BookPickEntity bookPickEntity) {
    return new BookPick(
        bookPickEntity.getBookPickId(),
        memberConverter.toDomain(bookPickEntity.getMemberEntity()),
        bookConverter.toDomain(bookPickEntity.getBookEntity()));
  }
}
