package com.onetuks.libraryauth.oauth.strategy.dto.user_info;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonNaming(SnakeCaseStrategy.class)
public class GoogleUserInfo {

  private String sub;
  private String email;
  private String name;

  public GoogleUserInfo(String sub, String email, String name) {
    this.sub = sub;
    this.email = email;
    this.name = name;
  }
}
