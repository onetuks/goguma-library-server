package com.onetuks.coreauth.oauth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonNaming(SnakeCaseStrategy.class)
public class NaverUser {

  private String resultCode;
  private String message;
  private Response response;

  public NaverUser(String resultCode, String message, Response response) {
    this.resultCode = resultCode;
    this.message = message;
    this.response = response;
  }

  @Getter
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  @JsonNaming(SnakeCaseStrategy.class)
  public static class Response {

    private String id;
    private String email;
    private String name;

    public Response(String id, String email, String name) {
      this.id = id;
      this.email = email;
      this.name = name;
    }
  }
}
