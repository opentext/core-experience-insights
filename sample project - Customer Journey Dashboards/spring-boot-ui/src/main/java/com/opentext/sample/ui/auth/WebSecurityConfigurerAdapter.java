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
package com.opentext.sample.ui.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class WebSecurityConfigurerAdapter {

  /** Content Security Protocol header per config. Same as CXI. */
  @Value("${rest-server.csp-header:''}")
  private String cspString;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    // OPTIONAL: single-spa apps require CORS. See {@link CORSConfig}.
    http.cors();

    // impose CSRF except where noted
    http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

    // Session fixation protection
    http.sessionManagement().sessionFixation().migrateSession();

    // Add security headers:
    //     CSP, "X-Content-Type-Options": "nosniff", "X-Frame-Options": "DENY", "X-XSS-Protection": "1; mode=block"
    http.headers()
        .contentTypeOptions()
        .and()
        .frameOptions()
        .deny()
        .xssProtection()
        .block(false)
        .and()
        .contentSecurityPolicy(cspString);

    return http.build();
  }
}
