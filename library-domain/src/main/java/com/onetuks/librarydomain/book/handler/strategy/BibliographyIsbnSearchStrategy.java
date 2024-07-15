package com.onetuks.librarydomain.book.handler.strategy;

import com.onetuks.librarydomain.book.handler.dto.IsbnResult;
import com.onetuks.librarydomain.book.handler.strategy.dto.BibliographyQueryResponse;
import com.onetuks.libraryobject.config.WebClientConfig;
import com.onetuks.libraryobject.error.ErrorCode;
import com.onetuks.libraryobject.util.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
@ComponentScan(basePackageClasses = WebClientConfig.class)
public class BibliographyIsbnSearchStrategy implements IsbnSearchStrategy {

  private static final String BIBLIOGRAPHY_QUERY_URL = "https://www.nl.go.kr/seoji/SearchApi.do";

  @Value("${openapi.center-lib.secret-key}")
  private String secretKey;

  private final WebClient webClient;
  private final URIBuilder uriBuilder;

  public BibliographyIsbnSearchStrategy(WebClient webClient, URIBuilder uriBuilder) {
    this.webClient = webClient;
    this.uriBuilder = uriBuilder;
  }

  @Override
  public IsbnResult process(String isbn) {
    return webClient
        .get()
        .uri(uriBuilder.buildUri(BIBLIOGRAPHY_QUERY_URL, buildMultiValueMap(isbn)))
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
        .bodyToMono(BibliographyQueryResponse.class)
        .map(IsbnResult::from)
        .block();
  }

  @Override
  public MultiValueMap<String, String> buildMultiValueMap(String isbn) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("cert_key", secretKey);
    params.add("result_style", "json");
    params.add("page_no", "1");
    params.add("page_size", "10");
    params.add("isbn", isbn);
    return params;
  }
}
