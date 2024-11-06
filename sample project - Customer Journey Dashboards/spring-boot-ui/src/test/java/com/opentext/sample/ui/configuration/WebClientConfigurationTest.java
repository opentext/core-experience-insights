package com.opentext.sample.ui.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;

@ActiveProfiles("testGCP")
@SpringBootTest
class WebClientConfigurationTest {

  @Autowired
  @Qualifier("plainWebClient")
  private WebClient plainWebClient;

  @Autowired private WebClient webClient;

  @Test
  void plainWebClientLoads() {
    assertThat(plainWebClient).isNotNull();
  }

  @Test
  void webClientLoads() {
    assertThat(webClient).isNotNull();
  }
}
