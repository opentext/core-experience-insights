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
package com.opentext.sample.ui.model;

import java.io.Serializable;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Data
public class QueryResponse implements Serializable {
  private static final long serialVersionUID = -3881121245189111360L;

  private List<JsonNode> rows;

  private JsonNode page;

  @JsonProperty("@first")
  private String first;

  @JsonProperty("@next")
  private String next;

  @JsonProperty("@self")
  private String self;

  @Value
  @Builder
  public static class Metadata {
    int pageSize;
    String pageState;
    int numberOfElements;
    boolean first;
    boolean last;
  }
}
