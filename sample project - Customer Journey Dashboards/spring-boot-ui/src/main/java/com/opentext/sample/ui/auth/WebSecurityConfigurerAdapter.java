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

import java.io.IOException;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class WebSecurityConfigurerAdapter {

  /** Content Security Protocol header per config. Same as CXI. */
  @Value("${rest-server.csp-header:''}")
  private String cspString;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    // OPTIONAL: single-spa apps require CORS. See {@link CORSConfig}.
    http.cors(Customizer.withDefaults());

    final CookieCsrfTokenRepository csrfCookieRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
    csrfCookieRepository.setCookieCustomizer(cookieCustomizer -> cookieCustomizer.sameSite("Lax"));
    csrfCookieRepository.setHeaderName("X-XSRF-TOKEN");

    // impose CSRF except where noted
    http.csrf(
        csrf ->
            csrf.csrfTokenRepository(csrfCookieRepository).csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler()));

    // impose CSRF except where noted
    http.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));

    // Session fixation protection
    http.sessionManagement(
        sessionManagementCustomizer -> sessionManagementCustomizer.sessionFixation().migrateSession());

    // Add security headers:
    http.headers(
        headerCustomizer ->
            headerCustomizer
                .referrerPolicy(referrerPolicy -> referrerPolicy.policy(ReferrerPolicy.SAME_ORIGIN))
                // "X-XSS-Protection": "1; mode=block"
                .xssProtection(xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
                // "X-Content-Type-Options": "nosniff"
                .contentTypeOptions(Customizer.withDefaults())
                // "X-Frame-Options": "DENY"
                .frameOptions(FrameOptionsConfig::deny)
                // CSP
                .contentSecurityPolicy(csp -> csp.policyDirectives(this.cspString)));

    return http.build();
  }

  final class SpaCsrfTokenRequestHandler extends CsrfTokenRequestAttributeHandler {
    private final CsrfTokenRequestHandler delegate = new XorCsrfTokenRequestAttributeHandler();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> csrfToken) {
      /*
       * Always use XorCsrfTokenRequestAttributeHandler to provide BREACH protection of
       * the CsrfToken when it is rendered in the response body.
       */
      this.delegate.handle(request, response, csrfToken);
    }

    @Override
    public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {

      /*
       * If the request contains a request header, use CsrfTokenRequestAttributeHandler
       * to resolve the CsrfToken. This applies when a single-page application includes
       * the header value automatically, which was obtained via a cookie containing the
       * raw CsrfToken.
       */
      if (StringUtils.hasText(request.getHeader(csrfToken.getHeaderName()))) {
        return super.resolveCsrfTokenValue(request, csrfToken);
      }

      /*
       * In all other cases (e.g. if the request contains a request parameter), use
       * XorCsrfTokenRequestAttributeHandler to resolve the CsrfToken. This applies
       * when a server-side rendered form includes the _csrf request parameter as a
       * hidden input.
       */
      return this.delegate.resolveCsrfTokenValue(request, csrfToken);
    }
  }

  final class CsrfCookieFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
        throws ServletException, IOException {
      CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
      // Render the token value to a cookie by causing the deferred token to be loaded
      csrfToken.getToken();

      filterChain.doFilter(request, response);
    }
  }
}
