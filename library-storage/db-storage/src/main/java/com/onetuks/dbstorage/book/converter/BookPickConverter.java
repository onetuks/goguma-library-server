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

  public BookPickEntity toEntity(BookPick model) {
    return new BookPickEntity(
        model.bookPickId(),
        memberConverter.toEntity(model.member()),
        bookConverter.toEntity(model.book()));
  }

  public BookPick toModel(BookPickEntity entity) {
    if (entity == null) {
      return null;
    }

    return new BookPick(
        entity.getBookPickId(),
        memberConverter.toModel(entity.getMemberEntity()),
        bookConverter.toModel(entity.getBookEntity()));
  }
}
