package com.onetuks.librarydomain.book.handler.dto.vo;

public record IntroductionData(String value) implements IsbnResultData {

  @Override
  public String filterDataValue(String newValue) {
    return isNullOrBlank(newValue) ? value : regulateValue(newValue);
  }

  private String regulateValue(String value) {
    return value.replace("\r", "");
  }
}
