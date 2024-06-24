package com.onetuks.coreweb.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.LoggingCodecSupport;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Slf4j
@Configuration
public class WebClientConfig {

  @Bean
  public WebClient webClient() {
    return WebClient.builder()
        .clientConnector(new ReactorClientHttpConnector(httpClient()))
        .exchangeStrategies(exchangeStrategies())
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .filter(
            ExchangeFilterFunction.ofRequestProcessor(
                clientRequest -> {
                  log.debug("Request: {} {}", clientRequest.method(), clientRequest.url());
                  clientRequest
                      .headers()
                      .forEach(
                          (name, values) ->
                              values.forEach(value -> log.debug("{} : {}", name, value)));
                  return Mono.just(clientRequest);
                }))
        .filter(
            ExchangeFilterFunction.ofResponseProcessor(
                clientResponse -> {
                  clientResponse
                      .headers()
                      .asHttpHeaders()
                      .forEach(
                          (name, values) ->
                              values.forEach(value -> log.debug("{} : {}", name, value)));
                  return Mono.just(clientResponse);
                }))
        .build();
  }

  @Bean
  public HttpClient httpClient() {
    return HttpClient.create(connectionProvider())
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1_000 * 30)
        .responseTimeout(Duration.ofSeconds(60))
        .doOnConnected(
            connection ->
                connection
                    .addHandlerLast(new ReadTimeoutHandler(30_000_000, TimeUnit.SECONDS))
                    .addHandlerLast(new WriteTimeoutHandler(30_000_000, TimeUnit.SECONDS)));
  }

  @Bean
  public ExchangeStrategies exchangeStrategies() {
    ExchangeStrategies exchangeStrategies =
        ExchangeStrategies.builder()
            .codecs(
                clientCodecConfigurer ->
                    clientCodecConfigurer.defaultCodecs().maxInMemorySize(1024 * 1024 * 50))
            .build();

    exchangeStrategies.messageWriters().stream()
        .filter(LoggingCodecSupport.class::isInstance)
        .forEach(
            httpMessageWriter ->
                ((LoggingCodecSupport) httpMessageWriter).setEnableLoggingRequestDetails(true));

    return exchangeStrategies;
  }

  @Bean
  public ConnectionProvider connectionProvider() {
    return ConnectionProvider.builder("http-pool")
        .maxConnections(5_000)
        .pendingAcquireTimeout(Duration.ofMillis(45_000))
        .pendingAcquireMaxCount(100)
        .maxIdleTime(Duration.ofMillis(8_000L))
        .maxLifeTime(Duration.ofMillis(58_000L))
        .lifo()
        .build();
  }
}
