package com.onetuks.librarydomain.book.handler.strategy.dto;

import java.util.List;

public record BibliographyQueryResponse(
    String TOTAL_COUNT, List<BibliographyQueryData> docs, String PAGE_NO) {

  public record BibliographyQueryData(
      String PUBLISHER,
      String DDC,
      String UPDATE_DATE,
      String EA_ADD_CODE,
      String PUBLISHER_URL,
      String AUTHOR,
      String SERIES_TITLE,
      String KDC,
      String EDITION_STMT,
      String BOOK_TB_CNT_URL,
      String SET_ISBN,
      String REAL_PUBLISH_DATE,
      String TITLE_URL,
      String PRE_PRICE,
      String BOOK_INTRODUCTION_URL,
      String DEPOSIT_YN,
      String BOOK_SIZE,
      String BOOK_SUMMARY_URL,
      String EBOOK_YN,
      String REAL_PRICE,
      String FORM,
      String FORM_DETAIL,
      String PAGE,
      String CONTROL_NO,
      String SERIES_NO,
      String EA_ISBN,
      String INPUT_DATE,
      String SET_EXPRESSION,
      String VOL,
      String CIP_YN,
      String SUBJECT,
      String BIB_YN,
      String TITLE,
      String PUBLISH_PREDATE,
      String RELATED_ISBN,
      String SET_ADD_CODE) {}
}
