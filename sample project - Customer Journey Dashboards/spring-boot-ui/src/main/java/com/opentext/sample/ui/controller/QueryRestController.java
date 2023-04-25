// ************************************************************************************************************
//
// Copyright (c) 2021 Open Text. All Rights Reserved.
// Trademarks owned by Open Text.
//
// No part of this document may be photocopied, reproduced, translated, or transmitted in any form
// or by any means without the prior written consent of OpenText. Any unauthorized duplication
// in whole or in part is strictly prohibited by United States federal law. OpenText will enforce
// its copyright to the full extent of the law.
//
// ************************************************************************************************************

package com.opentext.sample.ui.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import com.opentext.sample.ui.configuration.ApiServerConfigProperties;
import com.opentext.sample.ui.model.QueryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/** REST calls. */
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "${rest-server.api-prefix}/${rest-server.version-name}")
@Slf4j
public class QueryRestController {

  /** Application configuration mapping class. */
  private final ApiServerConfigProperties config;

  /** The {@link WebClient}. Will be assigned by constructor via the {@link WebClientConfig}. */
  @Qualifier("plainWebClient")
  private final WebClient webClient;

  /**
   * Return query response entity for given query. It returns all records as fetch size is set to -1
   *
   * @return {@link QueryResponse}
   */
  @PostMapping(
      value = "/query",
      produces = {"application/json"})
  public Mono<QueryResponse> getQueryResponse(@RequestBody String query) {
    if (log.isTraceEnabled()) log.trace("getQueryResponse for {}", query);
    final String contextPath = config.getContext();

    // this is for demo purposes. Normally you'd want to create a service layer with defaults for urls, errors,
    // WebClient, etc.
    return this.webClient
        .post()
        .uri(config.getBaseUrl() + config.getQuery())
        .bodyValue(query)
        .retrieve()
        .bodyToMono(QueryResponse.class)
        .map(
            response -> {
              if (response.getSelf() != null) {
                response.setSelf(response.getSelf().replace(contextPath, "/"));
              }

              if (response.getFirst() != null) {
                response.setFirst(response.getFirst().replace(contextPath, "/"));
              }

              if (response.getNext() != null) {
                response.setNext(response.getNext().replace(contextPath, "/"));
              }
              return response;
            });
  }
}
