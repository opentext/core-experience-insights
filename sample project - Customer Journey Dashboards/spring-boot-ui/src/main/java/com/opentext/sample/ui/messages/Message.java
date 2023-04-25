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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

/**
 * Message provides a convenient encapsulation of a localizable message contained in a resource bundle accessible
 * through a {@link MessageResource}. The class supports handling of message arguments specified in a referenced
 * message. Support of message arguments is also recursive in that any message arguments that are of type {@link
 * Message} will be resolved recursively when accessing the message referenced by a {@link Message} instance.
 */
@Value
@Builder
public class Message {

  private final MessageSource messageSource;
  private final String messageKey;
  @Singular private final List<Object> messageArgs;

  /**
   * Gets the referenced message from the contained {@link MessageSource} using a specified locale.
   *
   * @param locale the locale to get the message for
   * @return the message
   * @throws NoSuchMessageException if the referenced message is not found in the message source
   */
  public String getMessage(Locale locale) {
    return MessageUtils.getMessage(
        getMessageSource(), getMessageKey(), resolveMessageArgs(getMessageArgs(), locale), locale);
  }

  /**
   * Convenience method to resolve a list of message arguments for a specified locale. Any items in the argument list
   * that are of type {@link Message} will be resolved by calling their {@link #getMessage(Locale)} method. All other
   * arguments are returned as is.
   *
   * @param messageArgs the list of message arguments to resolve
   * @param locale the locale to resolve {@link Message} message arguments with
   * @return the list of message arguments with {@link Message} arguments resolved
   */
  public static List<Object> resolveMessageArgs(List<Object> messageArgs, Locale locale) {

    if (messageArgs == null) return messageArgs;

    List<Object> resolvedArgs = new ArrayList<>(messageArgs.size());
    for (Object messageArg : messageArgs) {
      resolvedArgs.add(messageArg instanceof Message ? ((Message) messageArg).getMessage(locale) : messageArg);
    }
    return resolvedArgs;
  }
}
