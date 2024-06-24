package com.onetuks.libraryobject.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

  // Global
  INTERNAL_SERVER_ERROR("G001", "Internal Server Error"),
  INVALID_INPUT_VALUE_ERROR("G002", "유효하지 않은 입력값입니다."),
  INVALID_METHOD_ERROR("G003", "Method Argument 가 적절하지 않습니다."),
  REQUEST_BODY_MISSING_ERROR("G004", "RequestBody 에 데이터가 존재하지 않습니다."),
  REQUEST_PARAM_MISSING_ERROR("G005", "RequestParam 에 데이터가 전달되지 않았습니다."),
  INVALID_TYPE_VALUE_ERROR("G006", "타입이 유효하지 않습니다."),
  NOT_FOUND_ENTITY("G007", "엔티티를 찾을 수 없습니다."),
  UTIL_NOT_CONSTRUCTOR("G008", "유틸클래스는 생성자를 호출할 수 없습니다."),
  FILE_NOT_FOUND("G009", "해당 파일을 찾을 수 없습니다."),
  DUPLICATED_COLUMN_VALUE("G010", "이미 존재하는 값으로 설정할 수 없습니다."),

  ILLEGAL_ARGUMENT_ERROR("E001", "잘못된 입력입니다."),
  ILLEGAL_STATE_ERROR("E002", "잘못된 상태입니다"),
  JSON_CONVERT_ERROR("E003", "Json 변환 중 에러가 발생했습니다."),
  OPENAPI_REQUEST_ERROR("E004", "OpenApi 요청 중 에러가 발생했습니다."),
  VERIFICATION_FAIL_ERROR("E005", "검증에 실패한 유효하지 않은 데이터입니다."),

  // Auth
  EXPIRED_TOKEN("L001", "토큰이 만료되었습니다."),
  EXPIRED_REFRESH_TOKEN("L002", "리프레쉬 토큰도 만료되어 다시 로그인을 요청합니다."),
  UNAUTHORIZED_TOKEN("L003", "인증되지 않은 토큰입니다."),
  OAUTH_CLIENT_SERVER_ERROR("L004", "oauth 클라이언트 서버 에러입니다."),
  IS_LOGOUT_TOKEN("L005", "이미 로그아웃한 토큰입니다."),
  EMPTY_HEADER_AUTHORIZATION("L006", "헤더에 Authorization 이 존재하지 않습니다."),
  UNAUTHORITY_ACCESS_DENIED("L007", "API 접근 권한이 없습니다."),
  ;

  private final String code;
  private final String message;

  ErrorCode(String code, String message) {
    this.code = code;
    this.message = message;
  }
}
