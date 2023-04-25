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
package com.opentext.sample.ui.controller;

import java.io.IOException;
import java.util.Locale;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import com.opentext.sample.ui.configuration.Constants;
import com.opentext.sample.ui.exception.RestErrorMessage;
import com.opentext.sample.ui.exception.RestException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
@RestController
public class UiErrorController implements ErrorController {

  /**
   * Overrides the default, which is to look for a local /error directory with files named 40x.html.
   *
   * @param request injected
   * @param response injected
   */
  @GetMapping(value = "/error")
  public void handleError(HttpServletRequest request, HttpServletResponse response) {
    log.error("Error controller in action");
    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    try {
      if (status != null) {
        Integer statusCode = Integer.valueOf(status.toString());
        log.error("The UIErrorController has received an error status of {}", statusCode);
        // Handle XHR errors with response headers only, not a redirect
        if (isXHRRequest(request)) {
          response.setStatus(statusCode);
        } else {
          response.sendRedirect(Constants.OOPS_ERROR_URL + statusCode);
        }
      } else {
        response.sendRedirect(Constants.OOPS_ERROR_URL + "unknown");
      }
    } catch (IOException e) {
      log.error("Can't redirect error: ", e);
    }
  }

  // Routed from the WebClient filter for RestExceptions
  @ExceptionHandler(RestException.class)
  public ResponseEntity<RestErrorMessage> handleRestException(RestException ex, Locale locale) {
    return handleLocalizableRuntimeException(ex.getHttpStatus(), ex.getHeaders(), ex, locale);
  }

  // Routed from the WebClient filter for everything else
  @ExceptionHandler(WebClientResponseException.class)
  public ResponseEntity<RestErrorMessage> handleWebClientResponseException(RestException ex, Locale locale) {
    return handleLocalizableRuntimeException(ex.getHttpStatus(), ex.getHeaders(), ex, locale);
  }

  /**
   * Exception Handler method.
   *
   * @param httpStatus
   * @param headers
   * @param ex
   * @param locale
   * @return
   */
  private ResponseEntity<RestErrorMessage> handleLocalizableRuntimeException(
      HttpStatus httpStatus, HttpHeaders headers, RuntimeException ex, Locale locale) {

    String message = null;
    if (message == null) message = ex.getMessage();
    if (message == null && ex.getCause() != null) message = ex.getCause().getMessage();
    if (message == null && httpStatus != null) message = httpStatus.getReasonPhrase();

    return RestErrorMessage.builder()
        .status(httpStatus)
        .headers(headers)
        .displayMessage(message)
        .build()
        .toResponseEntity();
  }

  /**
   * Tests for XHR Request headers.
   *
   * @param request
   * @return boolean if XHR request
   */
  private static boolean isXHRRequest(HttpServletRequest request) {
    String requestedWithHeader = request.getHeader(Constants.X_REQUESTED_HEADER);
    return (Constants.XHR_REQUEST_TYPE.equals(requestedWithHeader));
  }
}
