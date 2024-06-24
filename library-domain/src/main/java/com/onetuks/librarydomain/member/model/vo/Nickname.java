package com.onetuks.librarydomain.member.model.vo;

import java.util.List;

public record Nickname(String value) {

  private static final List<String> FORBIDDEN_TOKENS =
      List.of("admin", "administrator", "root", "관리자", "운영자", "시스템");
  private static final List<String> SPECIAL_CHARACTERS =
      List.of("!", "@", "#", "$", "%", "^", "&", "*");

  public Nickname {
    if (value != null) {
      validateNicknameValueForbiddenToken(value);
      validateNicknameValueSpecialCharacter(value);
    }
  }

  private void validateNicknameValueSpecialCharacter(String value) {
    if (SPECIAL_CHARACTERS.stream().anyMatch(value::contains)) {
      throw new IllegalArgumentException("특수문자는 사용할 수 없습니다.");
    }
  }

  private void validateNicknameValueForbiddenToken(String value) {
    if (FORBIDDEN_TOKENS.contains(value.toLowerCase())) {
      throw new IllegalArgumentException("사용할 수 없는 닉네임입니다.");
    }
  }
}
