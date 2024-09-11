package com.onetuks.librarydomain.member.model.vo;

import com.onetuks.libraryobject.annotation.Generated;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record Nickname(String value) {

  private static final String DEFAULT_NICKNAME = "고구마 침팬치";
  private static final List<String> FORBIDDEN_TOKENS =
      List.of("admin", "administrator", "root", "관리자", "운영자", "시스템");
  private static final List<String> SPECIAL_CHARACTERS =
      List.of("`", "~", "@", "#", "$", "^", "+", "=", "<", ">", "/", ";", "\\'", "\"", "|", "\\");

  public Nickname {
    if (value == null) {
      value = DEFAULT_NICKNAME + UUID.randomUUID().toString().substring(0, 5).replace("-", "");
    }
    validateNicknameValueForbiddenToken(value);
    validateNicknameValueSpecialCharacter(value);
  }

  public static Nickname init() {
    return new Nickname(null);
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

  @Override
  @Generated
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Nickname nickname = (Nickname) o;
    return Objects.equals(value, nickname.value);
  }

  @Override
  @Generated
  public int hashCode() {
    return Objects.hashCode(value);
  }
}
