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

package com.opentext.sample.ui.controller;

import java.util.Locale;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.opentext.sample.ui.configuration.Constants;
import com.opentext.sample.ui.configuration.RestConfigProperties;
import com.opentext.sample.ui.messages.UiMessages;

/**
 * This controller deals with endpoints that are non-REST based, mainly those associated with the ReactJS app itself.
 * One exception is the basic /health ping endpoint. This has no dependencies on anything else in this application.
 */
@Controller
public class HomeController {

  private static final String INDEX_PAGE = "index.html";

  private static final String OOPS_PAGE = "error.html";

  private final UiMessages messages;

  private final String apiPath;

  /**
   * Inject config beans needed for React-based renders.
   *
   * @param messages
   * @param restConfig
   * @param userUtil
   */
  public HomeController(UiMessages messages, RestConfigProperties restConfig) {
    this.messages = messages;
    this.apiPath = restConfig.getApiPrefix() + Constants.PATH_DELIMITER + restConfig.getVersionName();
  }

  /**
   * ReactJS home page.
   *
   * @param model
   * @param locale
   * @return
   */
  @GetMapping("/")
  public String index(Model model, Locale locale) {
    buildUserModel(model, locale);
    return INDEX_PAGE;
  }

  /**
   * Returns the oops error page application
   *
   * @return
   */
  @GetMapping(value = "/oops")
  public String noauth(Model model, Locale locale) {
    buildUserModel(model, locale);
    return OOPS_PAGE;
  }

  /**
   * Shared model assembly.
   *
   * @param model
   * @param locale
   */
  protected void buildUserModel(Model model, Locale locale) {
    String messageBundleJson = messages.getAllMessagesJson(locale);
    model.addAttribute(Constants.ENV_MESSAGES_KEY, messageBundleJson);
    model.addAttribute(Constants.ENV_REST_URI, this.apiPath);
  }
}
