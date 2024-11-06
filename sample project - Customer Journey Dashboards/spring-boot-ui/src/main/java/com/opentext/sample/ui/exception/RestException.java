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
package com.opentext.sample.ui.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RestException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final HttpStatusCode httpStatus;
  private final HttpHeaders headers;
  private final boolean retryable;

  protected RestException(HttpStatusCode status, HttpHeaders headers, String message, Throwable t) {
    this(status, headers, message, false, t);
  }

  @Builder
  private RestException(
      HttpStatusCode httpStatus, HttpHeaders headers, String message, boolean retryable, Throwable cause) {
    this.httpStatus = (httpStatus == null) ? HttpStatus.BAD_REQUEST : httpStatus;
    this.headers = headers;
    this.retryable = retryable;
  }

  public static class RestExceptionBuilder {}
}
