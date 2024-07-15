package com.onetuks.librarydomain.book.handler.dto.vo;

public record IsbnData(String value) implements IsbnResultData {

  @Override
  public String filterDataValue(String newValue) {
    String regulatedValue = regulateIsbnValue(newValue);
    return isNullOrBlank(regulatedValue) || containsSpecialCharacter(regulatedValue)
        ? value
        : regulatedValue;
  }

  private String regulateIsbnValue(String value) {
    return value.replaceAll("-", "");
  }
}
