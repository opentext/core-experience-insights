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
package com.opentext.sample.ui.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import com.opentext.sample.ui.auth.OAuth2Constants;
import com.opentext.sample.ui.configuration.ApiServerConfigProperties;
import com.opentext.sample.ui.configuration.WebClientConfiguration;
import com.opentext.sample.ui.model.TokenResponse;
import lombok.extern.slf4j.Slf4j;

/** This is a simple utility class to fetch OAuth2 tokens from the configured endpoint. */
@Component
@Slf4j
public class OAuth2Util {

  /** The cached URL of the OAuth2 server. */
  private static String CACHED_URL_STRING;

  /** The cached token of the OAuth2 server. */
  private static String CACHED_SOON_TO_EXPIRE_TOKEN;

  /** Configuration for report server. */
  private final ApiServerConfigProperties config;

  /** Simple WebClient, not the one configured in {@link WebClientConfiguration}. */
  private final WebClient webClient;

  public OAuth2Util(ApiServerConfigProperties config) {
    this.config = config;
    this.webClient = WebClient.builder().build();
  }

  /**
   * WARNING: If api-server.oauth2.cache.enabled is true then oauth2 token caching is applied, but no checks are in
   * place to check for expired tokens. This is not an ideal solution.
   *
   * <p>An alternate approach would be to impose the same OAUTH login on this application and just propagate the JWT
   * forward to the requested API. But as is, this is an unprotected application.
   *
   * @return
   */
  public String getToken() {
    if (config.getOauth2().isCachable() && null != CACHED_SOON_TO_EXPIRE_TOKEN) return CACHED_SOON_TO_EXPIRE_TOKEN;

    if (log.isTraceEnabled()) log.trace("Getting bearer token from {}", getTokenUri());

    TokenResponse tokenResponse =
        this.webClient
            .post()
            .uri(getTokenUri())
            .headers(
                httpHeaders -> httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE))
            .retrieve()
            .bodyToMono(TokenResponse.class)
            .block();

    Assert.notNull(tokenResponse, "Invalid token response from " + getTokenUri());

    if (config.getOauth2().isCachable()) {
      CACHED_SOON_TO_EXPIRE_TOKEN = tokenResponse.getAccessToken();
      return CACHED_SOON_TO_EXPIRE_TOKEN;
    }
    return tokenResponse.getAccessToken();
  }

  /**
   * Build URL String for configured OAuth2 Provider. It's safe to cache this value since it's entirely
   * configuration-based.
   *
   * @return URL
   */
  private String getTokenUri() {
    if (null != CACHED_URL_STRING) return CACHED_URL_STRING;

    String grantType = config.getOauth2().getGrantType();
    UriComponentsBuilder builder =
        UriComponentsBuilder.fromUriString(config.getOauth2().getTokenUrl())
            .queryParam(OAuth2Constants.GRANT_TYPE_PARAMETER, grantType)
            .queryParam(OAuth2Constants.CLIENT_ID_PARAMETER, config.getOauth2().getClientId())
            .queryParam(OAuth2Constants.CLIENT_SECRET_PARAMETER, config.getOauth2().getClientSecret());

    if (OAuth2Constants.GRANT_TYPE_PASSWORD_PARAMETER_VALUE.equals(grantType)) {
      builder
          .queryParam(OAuth2Constants.USERNAME_PARAMETER, config.getOauth2().getUsername())
          .queryParam(OAuth2Constants.PASSWORD_PARAMETER, config.getOauth2().getPassword());
    }

    CACHED_URL_STRING = builder.toUriString();
    return CACHED_URL_STRING;
  }
}
