package com.onetuks.libraryexternal.book.handler.dto.vo;

public record KdcData(String value) implements IsbnResultData {

  @Override
  public String filterDataValue(String newValue) {
    String regulatedValue = regulateKdcValue(newValue);
    return isNullOrBlank(regulatedValue)
            || containsSpecialCharacter(regulatedValue)
            || isMoreAbstract(regulatedValue)
        ? value
        : regulatedValue;
  }

  private boolean isMoreAbstract(String value) {
    if (this.value == null || value.isBlank()) {
      return false;
    }
    return value.length() < this.value.length();
  }

  private String regulateKdcValue(String value) {
    return value.split("\\.")[0];
  }
}
