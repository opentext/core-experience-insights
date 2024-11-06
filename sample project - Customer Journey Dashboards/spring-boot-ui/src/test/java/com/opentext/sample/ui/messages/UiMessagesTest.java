package com.opentext.sample.ui.messages;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("testGCP")
@SpringBootTest
class UiMessagesTest {

  @Mock ExposedResourceBundleMessageSource messageSource;

  @Test
  void uiMessageTests() {
    UiMessages uiMessages = new UiMessages(messageSource);
    when(messageSource.getKeys("messages/AppMessages", null)).thenReturn(null);
    assertThat(uiMessages.toString()).isNotNull();
    assertThat(uiMessages.getMessageJson("AppMessages", null)).isNotNull();
  }
}
