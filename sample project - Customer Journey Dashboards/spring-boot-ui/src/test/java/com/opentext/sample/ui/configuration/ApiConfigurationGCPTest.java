package com.opentext.sample.ui.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("testGCP")
@SpringBootTest
class ApiConfigurationGCPTest {

  @Autowired private ApiServerConfigProperties apiConfig;

  @Test
  void uiErrorContextLoads() {
    assertThat(apiConfig).isNotNull();
  }

  @Test
  void testTenantId() {
    assertThat(apiConfig.getTenantId()).isEqualTo("GCPTenantId");
  }

  @Test
  void testTokenUrl() {
    assertThat(apiConfig.getTokenUrl())
        .isEqualTo("https://experiencecenterapi.foo.bar/tenants/GCPTenantId/oauth2/token");
  }

  @Test
  void testClientIdAndSecret() {
    assertThat(apiConfig.getClientId()).isEqualTo("testGCPClientId");
    assertThat(apiConfig.getClientSecret()).isEqualTo("testGCPClientSecret");
  }

  @Test
  void testContext() {
    assertThat(apiConfig.getApiContextPath()).isEqualTo("/journey");
  }

  @Test
  void testAuthUrl() {
    assertThat(apiConfig.getAuthUrl()).isEqualTo("https://experiencecenterapi.foo.bar");
  }

  @Test
  void testQueryUrl() {
    assertThat(apiConfig.getQueryUrl())
        .isEqualTo("https://experiencecenterapi.foo.bar/journey/api/v1/bd/analyses/query?count=-1");
  }
}
