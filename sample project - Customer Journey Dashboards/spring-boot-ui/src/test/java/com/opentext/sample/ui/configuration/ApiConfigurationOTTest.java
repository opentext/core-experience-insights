package com.opentext.sample.ui.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("testOT")
@SpringBootTest
class ApiConfigurationOTTest {

  @Autowired private ApiServerConfigProperties apiConfig;

  @Test
  void uiErrorContextLoads() {
    assertThat(apiConfig).isNotNull();
  }

  @Test
  void testTenantId() {
    assertThat(apiConfig.getTenantId()).isNull();
  }

  @Test
  void testTokenUrl() {
    assertThat(apiConfig.getTokenUrl()).isEqualTo("http://dev.otds.com:8080/otdsws/oauth2/token");
  }

  @Test
  void testClientIdAndSecret() {
    assertThat(apiConfig.getClientId()).isEqualTo("testOtClientId");
    assertThat(apiConfig.getClientSecret()).isEqualTo("testOtClientSecret");
  }

  @Test
  void testContext() {
    assertThat(apiConfig.getApiContextPath()).isEqualTo("/");
  }

  @Test
  void testAuthUrl() {
    assertThat(apiConfig.getAuthUrl()).isEqualTo("http://dev.otds.com:8080");
  }

  @Test
  void testQueryUrl() {
    assertThat(apiConfig.getQueryUrl()).isEqualTo("https://apihost/api/v1/bd/analyses/query?count=-1");
  }
}
