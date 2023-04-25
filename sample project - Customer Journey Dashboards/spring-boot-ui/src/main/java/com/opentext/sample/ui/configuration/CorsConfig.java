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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

  @Value("${management.endpoints.web.cors.allowed-origins}")
  private String allowedOrigins;

  @Value("${management.endpoints.web.cors.allowed-methods}")
  private String allowedMethods;


  /**
   * This is the CORS Configuration method.<br>
   *
   * @param restConfig
   * @return an {@link WebMvcConfigurer} class.
   */
  @Bean
  public WebMvcConfigurer corsConfigurer(RestConfigProperties restConfig) {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry
            .addMapping(restConfig.getApiPrefix() + Constants.PATH_DELIMITER + restConfig.getVersionName() + "/**")
            .allowedOrigins(allowedOrigins)
            .allowedMethods(allowedMethods);
      }
    };
  }
}
