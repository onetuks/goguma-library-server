package com.onetuks.librarydomain.book.handler.dto.vo;

public record TitleData(String value) implements IsbnResultData {

  @Override
  public String filterDataValue(String newValue) {
    return isNullOrBlank(newValue)
            || containsSpecialCharacter(newValue)
            || containsAuthorInfo(newValue)
        ? value
        : newValue;
  }

  private boolean containsAuthorInfo(String value) {
    return value.contains("저자")
        || value.contains(" 지음")
        || value.contains(" 시집")
        || value.contains(" 소설")
        || value.contains(" 에세이")
        || value.contains(" 잡지");
  }
}
