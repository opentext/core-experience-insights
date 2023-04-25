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
package com.opentext.sample.ui.messages;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;
import com.opentext.sample.ui.configuration.Constants;
import com.opentext.sample.ui.util.Json;

@Component
public class UiMessages {

  private static final String BASE_MESSAGE_PATH = "messages/";

  /** User paths. */
  private static final String[] MESSAGE_BUNDLE_NAMES = {
    Constants.NOAUTH_MESSAGE_KEY, "CommonMessages", "AppMessages", "CommonUiMessages"
  };

  public static final List<String> MESSAGE_LIST = Collections.unmodifiableList(Arrays.asList(MESSAGE_BUNDLE_NAMES));

  private final ExposedResourceBundleMessageSource messageSource;

  public UiMessages(ExposedResourceBundleMessageSource messageSource) {
    this.messageSource = messageSource;
  }

  /**
   * Message fetcher. Gets an individual bundle as Json.
   *
   * @param bundleName
   * @param locale
   * @return Json
   */
  public String getMessageJson(String bundleName, Locale locale) {
    return Json.toJson(getMessages(bundleName, locale));
  }

  /**
   * Message fetcher. Gets an individual bundle as a map.
   *
   * @param bundleName
   * @param locale
   * @return a Map<String, String>
   */
  private Map<String, String> getMessages(String bundleName, Locale locale) {
    Map<String, String> messageBundle = new HashMap<>();
    messageSource.setBasename(BASE_MESSAGE_PATH + bundleName);
    Set<String> keys = messageSource.getKeys(BASE_MESSAGE_PATH + bundleName, locale);
    if (null != keys) {
      keys.stream().forEach(key -> messageBundle.put(key, messageSource.getMessage(key, null, locale)));
    }

    return messageBundle;
  }

  /**
   * Message fetcher. Gets all messages as Json.
   *
   * @param bundleName
   * @param locale
   * @return Json
   */
  public String getAllMessagesJson(Locale locale) {
    return Json.toJson(getAllMessages(locale));
  }

  /**
   * Message fetcher. Combines all separate message files into a single bundle.
   *
   * @param locale
   * @return a JSON message object
   */
  private Map<String, String> getAllMessages(Locale locale) {
    Map<String, String> messageBundle = new HashMap<>();
    MESSAGE_LIST.stream().forEach(item -> messageBundle.putAll(getMessages(item, locale)));
    return messageBundle;
  }
}
