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
package com.opentext.sample.ui.messages;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

/** Extends {link @ResourceBundleMessageSource} to provide a getKeys methods. */
@Component
public class ExposedResourceBundleMessageSource extends ResourceBundleMessageSource {

  /**
   * Gets the set of bundle message keys.
   *
   * @param basename
   * @param locale
   * @return Set<String> message keys
   */
  public Set<String> getKeys(String basename, Locale locale) {
    ResourceBundle bundle = getResourceBundle(basename, locale);
    return (null != bundle) ? bundle.keySet() : null;
  }
}
