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
//
package com.opentext.sample.ui.auth;

public class OAuth2Constants {

  private OAuth2Constants() {
    throw new IllegalStateException("Utility class");
  }

  public static final String BEARER_WITH_SPACE_VALUE = "Bearer ";

  public static final String SCOPE_PARAMETER = "scope";

  public static final String CLIENT_ID_PARAMETER = "client_id";

  public static final String CLIENT_SECRET_PARAMETER = "client_secret";

  public static final String GRANT_TYPE_PARAMETER = "grant_type";

  public static final String USERNAME_PARAMETER = "username";

  /** False positive in Fortify scan. */
  public static final String PASSWORD_PARAMETER = "password";

  /** False positive in Fortify scan. */
  public static final String GRANT_TYPE_PASSWORD_PARAMETER_VALUE = "password";

  public static final String GRANT_TYPE_CLIENT_CREDENTIAL_PARAMETER_VALUE = "client_credentials";

  public static final String GROUP_CLAIM = "grp";

  public static final String NAME_CLAIM = "uid";

  public static final String ISSUER_CLAIM = "iss";
}
