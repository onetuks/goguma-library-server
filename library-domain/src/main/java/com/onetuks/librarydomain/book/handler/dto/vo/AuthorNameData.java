package com.onetuks.librarydomain.book.handler.dto.vo;

public record AuthorNameData(String value) implements IsbnResultData {

  @Override
  public String filterDataValue(String newValue) {
    String regulatedNewValue = regulateValue(newValue);
    return isNullOrBlank(regulatedNewValue) || containsSpecialCharacter(regulatedNewValue)
        ? value
        : regulatedNewValue;
  }

  private String regulateValue(String value) {
    return value
        .replace(":", "")
        .replace("지음", "")
        .replace("지은이", "")
        .replace("저자", "")
        .replace("글: ", "")
        .trim();
  }
}
