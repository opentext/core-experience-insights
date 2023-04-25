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
package com.opentext.sample.ui.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

/** Maps application properties found under "report-server" to property fields. */
@Component
@ConfigurationProperties(prefix = "api-server")
@Data
public class ApiServerConfigProperties {

  private String scheme;
  private String host;
  private String port;
  private String context;

  /**
   * Helper method to assemble the outbound base URL based on configuration.
   *
   * @return
   */
  public String getBaseUrl() {
    return scheme + "://" + host + ":" + port + context;
  }

  @Value("query")
  private String query;

  private OAuth2 oauth2;

  @Data
  public static class OAuth2 {

    @Value("token-url")
    private String tokenUrl;

    @Value("grant-type")
    private String grantType;

    @Value("client-id")
    private String clientId;

    @Value("client-secret")
    private String clientSecret;

    @Value("username")
    private String username;

    @Value("password")
    private String password;

    @Value("cachable")
    private boolean cachable;
  }
}
