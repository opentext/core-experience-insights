// ************************************************************************************************************
//
// Copyright (c) 2023 Open Text. All Rights Reserved.
// Trademarks owned by Open Text.
//
// No part of this document may be photocopied, reproduced, translated, or transmitted in any form
// or by any means without the prior written consent of OpenText. Any unauthorized duplication
// in whole or in part is strictly prohibited by United States federal law. OpenText will enforce
// its copyright to the full extent of the law.
//
// ************************************************************************************************************
package com.opentext.sample.ui.configuration;

import java.time.Instant;
import java.util.function.Consumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.opentext.sample.ui.auth.OAuth2Util;
import com.opentext.sample.ui.exception.RestException;
import com.opentext.sample.ui.util.Json;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Configures a {@link WebClient} bean for use in the service layer. This webClient will include a dynamically set OAuth
 * bearer token.
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class WebClientConfiguration {

  /** Utility component for fetching a live OAuth2 token. */
  private final OAuth2Util oauthUtil;

  /**
   * This is not an OAuth-aware webClient. It will include the necessary headers including the bearer token and error
   * handlers but it will not renew expired tokens.
   *
   * @param webClientBuilder
   * @return
   */
  @Bean("plainWebClient")
  public WebClient getWebClient(WebClient.Builder webClientBuilder) {
    return webClientBuilder
        .filter(getErrorFilter())
        .filter(addBearerToken())
        .defaultHeaders(getDefaultHeaders())
        .build();
  }

  /**
   * {@link ExchangeFilterFunction} to set a bearer token dynamically.
   *
   * @return ExchangeFilterFunction
   */
  private ExchangeFilterFunction addBearerToken() {
    return (request, next) ->
        next.exchange(
            ClientRequest.from(request).headers(headers -> headers.setBearerAuth(oauthUtil.getToken())).build());
  }

  /**
   * These headers are set on every request.
   *
   * @return HttpHeaders
   */
  private Consumer<HttpHeaders> getDefaultHeaders() {
    return headers -> {
      headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
      headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
      headers.set(HttpHeaders.ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().toLanguageTag());
      headers.setContentLanguage(LocaleContextHolder.getLocale());
    };
  }

  /** POJO for error responses. */
  @Data
  private static class WebClientErrorMapper {
    Instant timestamp;
    int status;
    String error;
    String message;
    String path;

    private static WebClientErrorMapper from(String body) {
      try {
        return Json.getMapper().readValue(body, WebClientErrorMapper.class);
      } catch (JsonProcessingException e) {
        return null;
      }
    }
  }

  /**
   * Error handler function for the webClient above.
   *
   * @return
   */
  // squid:S3776 - what's a little extra complexity?
  private ExchangeFilterFunction getErrorFilter() {
    return ExchangeFilterFunction.ofResponseProcessor(
        response -> {
          HttpStatus status = HttpStatus.valueOf(response.statusCode().value());
          if (HttpStatus.UNAUTHORIZED.equals(status) || HttpStatus.FORBIDDEN.equals(status)) {
            throw new InsufficientAuthenticationException(status.getReasonPhrase());
          } else if (!status.is2xxSuccessful()) {
            return response
                .bodyToMono(String.class) // Ingest the raw response data as a String
                .flatMap(
                    exceptionResponseBody -> {
                      // parse via JSON as a RestErrorMessage
                      WebClientErrorMapper webClientErrorMapper = WebClientErrorMapper.from(exceptionResponseBody);
                      String messageBody =
                          (null != webClientErrorMapper) ? webClientErrorMapper.getMessage() : status.getReasonPhrase();

                      log.error("Received an error response from REST call: {}", exceptionResponseBody);
                      return Mono.error(
                          RestException.builder()
                              .httpStatus(status)
                              .headers(response.headers().asHttpHeaders())
                              .message(messageBody)
                              .build());
                    });
          }
          return Mono.just(response);
        });
  }
}
