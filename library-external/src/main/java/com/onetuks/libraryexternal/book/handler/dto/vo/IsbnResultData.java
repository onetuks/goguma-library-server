package com.onetuks.libraryexternal.book.handler.dto.vo;

import java.util.List;

public interface IsbnResultData {

  List<String> SPECIAL_CHARACTERS =
      List.of("`", "~", "@", "#", "$", "^", "+", "=", "<", ">", "/", ";", "\\'", "\"", "|", "\\");

  String filterDataValue(String newValue);

  default boolean isNullOrBlank(String value) {
    return value == null || value.isBlank();
  }

  default boolean containsSpecialCharacter(String value) {
    return SPECIAL_CHARACTERS.stream().anyMatch(value::contains);
  }
}
