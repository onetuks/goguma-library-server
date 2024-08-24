package com.onetuks.librarydomain.book.handler.dto.vo;

public record CoverImageUrlData(String value) implements IsbnResultData {

  private static final String KOLIS_COVER_IMAGE_URL_PREFIX = "https://cover.nl.go.kr/";

  @Override
  public String filterDataValue(String newValue) {
    return isNullOrBlank(newValue) ? value : regulateValue(newValue);
  }

  private String regulateValue(String value) {
    return value.contains("https://") ? value : KOLIS_COVER_IMAGE_URL_PREFIX + value;
  }
}
