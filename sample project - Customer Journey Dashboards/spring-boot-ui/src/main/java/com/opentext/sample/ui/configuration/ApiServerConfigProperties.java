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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/** Maps application properties found under "api-server" to property fields. */
@Component
@ConfigurationProperties(prefix = "api-server")
@Data
@Slf4j
public class ApiServerConfigProperties {

  private static final String QUERY_DEFAULT = "/api/v1/bd/analyses/query?count=-1";
  private static final String OTDS_DEV_PREFIX_DEFAULT = "/otdsws";
  private static final String OTDS_TENANT_PREFIX_DEFAULT = "/otdstenant";
  private static final String GCP_OTDS_TENANT_PREFIX_DEFAULT = "/tenants";
  private static final String OTDS_TOKEN_DEFAULT = "/oauth2/token";
  private static final String OAUTH_GRANT_DEFAULT = "client_credentials";

  
  private String apiHost;

  /** The CXI API Gateway URL. */
  private String journeyUrl;

  /** The OTDS URL. */
  private String authUrl;

  private String tenantId;

  @Value("${grant-type:" + OAUTH_GRANT_DEFAULT + "}")
  private String grantType;

  private String clientId;

  private String clientSecret;

  /** Not recommended but if using grant-type=password, these are required. */
  private String username;

  /** @See username notes. */
  private String password;

  /** Generated field based on apiHost. */
  private String contextPath;

  /** Regular expression to extract the path from a URL. */
  private static final Pattern URL_PATTERN = Pattern.compile("https?://[^/]+(/[^?#]*)");

  /** Extract the contextPath at startup. */
  @PostConstruct
  public void initContextPath() {
    Matcher matcher = URL_PATTERN.matcher(this.journeyUrl);
    this.contextPath = matcher.find() ? matcher.group(1) : "/";
    if (log.isTraceEnabled()) {
      log.trace("initContextPath: setting contextPath {} ", this.contextPath);
    }
  }

  /**
   * The context path in the apiHost. For example, https://apiserver.com/path, then contextPath is "/path".
   *
   * @return cxi api server context path
   */
  public String getApiContextPath() {
    return this.contextPath;
  }

  /**
   * Helper method to assemble the outbound API URL based on configuration.
   *
   * @return api query URL
   */
  public String getQueryUrl() {
    return this.journeyUrl + QUERY_DEFAULT;
  }

  public boolean isGCP() {
    return this.journeyUrl.contains(this.apiHost);
  }

  /**
   * Helper method to assemble the outbound OTDS URL based on configuration.
   *
   * @return tokenUrl
   */
  public String getTokenUrl() {
    if (null == this.tenantId) {
      // multi-tenant OTDS, e.g., url + /otdsws/oauth2/token
      return this.authUrl + OTDS_DEV_PREFIX_DEFAULT + OTDS_TOKEN_DEFAULT;
    } else if (this.isGCP()) {
      // GCP
      return this.authUrl + GCP_OTDS_TENANT_PREFIX_DEFAULT + "/" + this.tenantId + OTDS_TOKEN_DEFAULT;
    } else {
      // OT
      return this.authUrl + OTDS_TENANT_PREFIX_DEFAULT + "/" + this.tenantId + OTDS_TOKEN_DEFAULT;
    }
  }
}
