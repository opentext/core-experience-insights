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

import java.util.Locale;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import com.opentext.sample.ui.configuration.Constants;
import com.opentext.sample.ui.exception.RestErrorMessage;
import com.opentext.sample.ui.exception.RestException;
import com.opentext.sample.ui.messages.UiMessages;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Controller
@Slf4j
public class UiErrorController implements ErrorController {

  private final UiMessages messages;

  private static final String STATUS_CODE_FIELD = "statusCode";
  private static final String ERROR_MESSAGE_FIELD = "errorMessage";

  /**
   * This will handle errors that the Spring Boot DispatcherServlet directs to its default error controller, which we're
   * overriding here. In the case of XHR requests, this will re-throw a RestException of the corresponding error code.
   * Otherwise this is an MVC method that returns the oops page with thymeleaf JS variables containing the status code
   * and a error message to be read by the SPA.
   *
   * @param model
   * @param request
   * @param response
   * @param locale
   */
  @GetMapping(value = "/error")
  public String handleError(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) {
    model.addAttribute("contextPath", request.getContextPath());
    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    if (status != null) {
      Integer statusCode = Integer.valueOf(status.toString());
      String errorMessage = "The application received an error status of " + statusCode;
      log.error(errorMessage);

      model.addAttribute(ERROR_MESSAGE_FIELD, errorMessage);
      model.addAttribute(STATUS_CODE_FIELD, statusCode);

      // Handle XHR errors with a standard RestException not an MVC page
      if (isXHRRequest(request)) {
        throw RestException.builder().httpStatus(HttpStatus.valueOf(statusCode)).message(errorMessage).build();
      }
    }
    String messageBundleJson = messages.getAllMessagesJson(locale);
    model.addAttribute(Constants.ENV_MESSAGES_KEY, messageBundleJson);
    return "error.html";
  }

  @InitBinder
  private void initBinder(WebDataBinder binder) {
    binder.setDisallowedFields();
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
      HttpStatusCode httpStatus, HttpHeaders headers, RuntimeException ex, Locale locale) {

    String message = null;
    if (message == null) {
      message = ex.getMessage();
    }
    if (message == null && ex.getCause() != null) {
      message = ex.getCause().getMessage();
    }
    if (message == null && httpStatus != null) {
      message = String.valueOf(httpStatus.value());
    }

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
