package com.onetuks.librarydomain.book.handler.dto.vo;

public record PublisherData(String value) implements IsbnResultData {

  @Override
  public String filterDataValue(String newValue) {
    return isNullOrBlank(newValue) || containsSpecialCharacter(newValue) || isEnglish(newValue)
        ? value
        : newValue;
  }

  private boolean isEnglish(String value) {
    return value
        .chars()
        .filter(c -> !Character.isWhitespace(c))
        .allMatch(
            c -> Character.isLetter(c) && (Character.isLowerCase(c) || Character.isUpperCase(c)));
  }
}
