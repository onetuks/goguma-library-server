package com.onetuks.libraryexternal.book.handler.strategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetuks.libraryexternal.book.handler.dto.IsbnResult;
import com.onetuks.libraryexternal.book.handler.strategy.dto.CollectionQueryResponse;
import com.onetuks.libraryexternal.util.URIBuilder;
import com.onetuks.libraryobject.error.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
public class CollectionIsbnSearchStrategy implements IsbnSearchStrategy {

  @Value(("${openapi.center-lib.secret-key}"))
  private String secretKey;

  private final WebClient webClient;
  private final URIBuilder uriBuilder;

  private final ObjectMapper objectMapper = new ObjectMapper();

  public CollectionIsbnSearchStrategy(WebClient webClient, URIBuilder uriBuilder) {
    this.webClient = webClient;
    this.uriBuilder = uriBuilder;
  }

  private static final String COLLECTION_QUERY_URL =
      "https://www.nl.go.kr/NL/search/openApi/search.do";

  @Override
  public IsbnResult process(String isbn) {
    return webClient
        .get()
        .uri(uriBuilder.buildUri(COLLECTION_QUERY_URL, buildMultiValueMap(isbn)))
        .retrieve()
        .onStatus(
            HttpStatusCode::isError,
            response ->
                Mono.error(
                    new WebClientResponseException(
                        ErrorCode.OPENAPI_REQUEST_ERROR.getMessage(),
                        response.statusCode().value(),
                        response.statusCode().toString(),
                        response.headers().asHttpHeaders(),
                        null,
                        null,
                        response.request())))
        .bodyToMono(String.class)
        .<CollectionQueryResponse>handle((body, sink) -> {
          try {
            sink.next(objectMapper.readValue(body, CollectionQueryResponse.class));
          } catch (JsonProcessingException e) {
            sink.error(new IllegalStateException("Json 파싱에 실패했습니다."));
          }
        })
        .map(IsbnResult::from)
        .block();
  }

  @Override
  public MultiValueMap<String, String> buildMultiValueMap(String isbn) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("key", secretKey);
    params.add("apiType", "json");
    params.add("pageNum", "1");
    params.add("pageSize", "10");
    params.add("detailSearch", "true");
    params.add("isbnOp", "isbn");
    params.add("isbnCode", isbn);
    return params;
  }
}
