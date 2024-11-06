package com.opentext.sample.ui.controller;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("testGCP")
@SpringBootTest
class ControllerTest {

  @Autowired private HomeController homeController;

  @Autowired private QueryRestController queryRestController;

  @Autowired private UiErrorController uiErrorController;

  @Test
  void homeContextLoads() {
    assertThat(homeController).isNotNull();
  }

  @Test
  void queryRestContextLoads() {
    assertThat(queryRestController).isNotNull();
  }

  @Test
  void uiErrorContextLoads() {
    assertThat(uiErrorController).isNotNull();
  }
}
