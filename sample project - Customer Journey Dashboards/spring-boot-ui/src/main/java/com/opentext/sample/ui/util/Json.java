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
package com.opentext.sample.ui.util;

import java.io.IOException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/** Json defines a singleton Jackson mapper for use by application. */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Json {

  public static class ParsingException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ParsingException(String reason) {
      super(reason);
    }

    public ParsingException(Throwable cause) {
      super(cause);
    }
  }

  private static ObjectMapper mapper;

  public static ObjectMapper getMapper() {
    if (mapper == null) {
      mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule());
      mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    return mapper;
  }

  /**
   * Convert object to json without (checked) exception.
   *
   * @param o the object.
   * @return the json.
   */
  public static String toJson(Object o) {
    try {
      return getMapper().writeValueAsString(o);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
      throw new ParsingException(e);
    }
  }
}
