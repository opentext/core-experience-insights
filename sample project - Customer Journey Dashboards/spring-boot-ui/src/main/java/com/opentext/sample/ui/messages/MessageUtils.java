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

import java.util.List;
import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

/** MessageUtils provides utility methods for working with {@link MessageSource} objects. */
public class MessageUtils {

  private MessageUtils() {}

  /**
   * Gets an optionally localized and parameterized message from a specified {@link MessageSource}.
   *
   * @param messageSource the {@link MessageSource} to get the message from
   * @param messageKey the message key of the message to get
   * @param messageArgs any arguments to the message
   * @param locale the locale of the message to get
   * @return the localized message or the value of messageKey if the message source or message key is null
   * @throws NoSuchMessageException if the specified message key is not found in the specified message source
   */
  @SuppressWarnings("squid:S3358") // S3358 - tolerate nested ternary
  public static String getMessage(
      MessageSource messageSource, String messageKey, List<Object> messageArgs, Locale locale) {
    return ((messageSource == null) || (messageKey == null))
        ? messageKey
        : messageSource.getMessage(messageKey, ((messageArgs == null) ? null : messageArgs.toArray()), locale);
  }
}
