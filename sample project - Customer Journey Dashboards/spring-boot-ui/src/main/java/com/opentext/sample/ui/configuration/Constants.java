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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** Assorted constants for the UI application. */
public final class Constants {

  private Constants() {
    // SQ hidden constructor
  }

  public static final String PATH_DELIMITER = "/";

  public static final String AUTHORIZATION_HEADER = "Authorization";

  public static final String X_CSRF_TOKEN = "X_CSRF_TOKEN";

  /** XHR Request Header. */
  public static final String X_REQUESTED_HEADER = "X-Requested-With";

  /** XHR Request Attribute. */
  public static final String XHR_REQUEST_TYPE = "XMLHttpRequest";

  /** XHR Request Error Header. */
  public static final String XHR_REQUEST_ERROR = "XHR_Request_Error";

  /** XHR Request Error Header Value. */
  public static final String XHR_REQUEST_CSRF_ERROR = "CSRF_Error";

  /** XHR Request Error Header Value. */
  public static final String XHR_REQUEST_UNAVAILABLE_ERROR = "Unavailable_Error";

  public static final String ENV_MESSAGES_KEY = "messages";

  public static final String ENV_REST_URI = "apiBaseUri";

  public static final String ENV_ERROR_MESSAGE = "errorMessage";

  public static final String HOME_TEMPLATE = "/";

  public static final String NOAUTH_MESSAGE_KEY = "NoAuthMessages";

  public static final String OOPS_ERROR_URL = "/oops/#/error/";

  /** User paths. */
  private static final String[] PUBLIC_PATHS_VALUES = {"/oops", "/error", "/favicon.ico", "/images", "/js", "/fonts"};

  // avoid mutable S2386
  public static final List<String> PUBLIC_PATHS = Collections.unmodifiableList(Arrays.asList(PUBLIC_PATHS_VALUES));
}
