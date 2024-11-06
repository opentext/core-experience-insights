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
package com.opentext.sample.ui.auth;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.opentext.sample.ui.configuration.ApiServerConfigProperties;
import com.opentext.sample.ui.configuration.WebClientConfiguration;
import com.opentext.sample.ui.model.TokenResponse;

import lombok.Data;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

/** This is a simple utility class to fetch OAuth2 tokens from the configured endpoint. */
@Component
@Slf4j
public class OAuth2Util {

  /** Configuration for report server. */
  private final ApiServerConfigProperties config;

  /**
   * Simple WebClient, not the one configured in {@link WebClientConfiguration}. We don't want it to attach a bearer
   * token.
   */
  private final WebClient webClient;

  /**
   * Constructor. Instantiates the webClient too.
   *
   * @param config the {@link ApiServerConfigProperties}
   */
  public OAuth2Util(ApiServerConfigProperties config) {
    this.config = config;
    this.webClient = WebClient.builder().build();
  }

  @Data
  @Builder
  public static class TokenBody {
    
    @JsonProperty("client_id")
    private final String clientId;
    
    @JsonProperty("client_secret")
    private final String clientSecret;
    
    @JsonProperty("grant_type")
    private final String grantType;

  }

  /**
   * This fires a new OAuth2 request on every API request. Not ideal, but this is a demo.
   *
   * @return accessToken as a String.
   */
  public String getToken() {
    String tokenUrl = config.getTokenUrl();

    
    TokenResponse tokenResponse = null;
    if (config.isGCP()) {
      
        TokenBody body = TokenBody.builder()
                      .clientId(config.getClientId())
                      .clientSecret(config.getClientSecret())
                      .grantType(config.getGrantType())
                      .build();

        tokenResponse = this.webClient
          .post()
          .uri(tokenUrl)
          .bodyValue(body)
          .headers(
              httpHeaders -> httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
          .retrieve()
          .bodyToMono(TokenResponse.class)
          .block();

    } else {
        tokenResponse = this.webClient
            .post()
            .uri(this.getTokenUrl())
            .headers(
                httpHeaders -> httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE))
            .retrieve()
            .bodyToMono(TokenResponse.class)
            .block();

    }
    Assert.notNull(tokenResponse, "Invalid token response from " + tokenUrl);

    return tokenResponse.getAccessToken();
  }

  /**
   * Build URL String for configured OAuth2 Provider.
   *
   * @return URL
   */
  private String getTokenUrl() {
    String grantType = config.getGrantType();
    UriComponentsBuilder builder =
        UriComponentsBuilder.fromUriString(config.getTokenUrl())
            .queryParam(OAuth2Constants.GRANT_TYPE_PARAMETER, grantType)
            .queryParam(OAuth2Constants.CLIENT_ID_PARAMETER, config.getClientId())
            .queryParam(OAuth2Constants.CLIENT_SECRET_PARAMETER, config.getClientSecret());

    if (OAuth2Constants.GRANT_TYPE_PASSWORD_PARAMETER_VALUE.equals(grantType)) {
      builder
          .queryParam(OAuth2Constants.USERNAME_PARAMETER, config.getUsername())
          .queryParam(OAuth2Constants.PASSWORD_PARAMETER, config.getPassword());
    }

    return builder.toUriString();
  }
}
