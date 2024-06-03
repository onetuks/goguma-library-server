package com.onetuks.coreauth.oauth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonNaming(SnakeCaseStrategy.class)
public class GoogleUser {

  private String sub;
  private String email;
  private String name;

  public GoogleUser(String sub, String email, String name) {
    this.sub = sub;
    this.email = email;
    this.name = name;
  }
}
