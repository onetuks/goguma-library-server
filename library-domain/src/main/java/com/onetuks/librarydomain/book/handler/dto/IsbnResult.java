package com.onetuks.librarydomain.book.handler.dto;

import com.onetuks.librarydomain.book.handler.dto.vo.AuthorNameData;
import com.onetuks.librarydomain.book.handler.dto.vo.CoverImageUrlData;
import com.onetuks.librarydomain.book.handler.dto.vo.IntroductionData;
import com.onetuks.librarydomain.book.handler.dto.vo.IsbnData;
import com.onetuks.librarydomain.book.handler.dto.vo.KdcData;
import com.onetuks.librarydomain.book.handler.dto.vo.PublisherData;
import com.onetuks.librarydomain.book.handler.dto.vo.TitleData;
import com.onetuks.librarydomain.book.handler.strategy.dto.BibliographyQueryResponse;
import com.onetuks.librarydomain.book.handler.strategy.dto.BibliographyQueryResponse.BibliographyQueryData;
import com.onetuks.librarydomain.book.handler.strategy.dto.CollectionQueryResponse;
import com.onetuks.librarydomain.book.handler.strategy.dto.CollectionQueryResponse.CollectionQueryData;

public record IsbnResult(
    String title,
    String authorName,
    String introduction,
    String publisher,
    String isbn,
    String kdc,
    String coverImageUrl) {

  public static IsbnResult from(BibliographyQueryResponse response) {
    if (response.docs().isEmpty()) {
      throw new IllegalStateException("해당 ISBN에 대한 도서 정보가 없습니다.");
    }

    BibliographyQueryData data = response.docs().getFirst();

    return new IsbnResult(
        data.SERIES_TITLE() + data.TITLE(),
        data.AUTHOR(),
        data.BOOK_INTRODUCTION(),
        data.PUBLISHER(),
        data.EA_ISBN(),
        data.KDC(),
        data.TITLE_URL());
  }

  public static IsbnResult from(CollectionQueryResponse response) {
    if (response.result().isEmpty()) {
      throw new IllegalStateException("해당 ISBN에 대한 도서 정보가 없습니다.");
    }

    CollectionQueryData data = response.result().getFirst();

    return new IsbnResult(
        data.titleInfo(),
        data.authorInfo(),
        null,
        data.pubInfo(),
        data.isbn(),
        data.classNo(),
        data.imageUrl());
  }

  public static IsbnResult init() {
    return new IsbnResult(null, null, null, null, null, null, null);
  }

  public IsbnResult update(IsbnResult isbnResult) {
    return new IsbnResult(
        new TitleData(title).filterDataValue(isbnResult.title()),
        new AuthorNameData(authorName).filterDataValue(isbnResult.authorName()),
        new IntroductionData(introduction).filterDataValue(isbnResult.introduction()),
        new PublisherData(publisher).filterDataValue(isbnResult.publisher()),
        new IsbnData(isbn).filterDataValue(isbnResult.isbn()),
        new KdcData(kdc).filterDataValue(isbnResult.kdc()),
        new CoverImageUrlData(coverImageUrl).filterDataValue(isbnResult.coverImageUrl()));
  }
}
