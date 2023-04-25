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
package com.opentext.sample.ui.configuration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Order(100)
@Configuration
public class TemplateConfig implements ApplicationContextAware {

  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  private static final String CHARACTER_ENCODING = "UTF-8";

  @Bean
  public ViewResolver htmlViewResolver() {
    ThymeleafViewResolver resolver = new ThymeleafViewResolver();
    resolver.setTemplateEngine(templateEngine());
    resolver.setContentType("text/html");
    resolver.setCharacterEncoding(CHARACTER_ENCODING);
    resolver.setViewNames(new String[] {"*.html"});
    return resolver;
  }

  @Bean
  public SpringTemplateEngine templateEngine() {
    SpringTemplateEngine engine = new SpringTemplateEngine();
    engine.setMessageSource(uiMessageSource());
    engine.addTemplateResolver(htmlTemplateResolver());
    engine.addTemplateResolver(javascriptTemplateResolver());
    return engine;
  }

  private ITemplateResolver htmlTemplateResolver() {
    SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
    resolver.setOrder(0);
    resolver.setCheckExistence(true);
    resolver.setApplicationContext(applicationContext);
    resolver.setPrefix("classpath:static/");
    resolver.setCacheable(false);
    resolver.setTemplateMode(TemplateMode.HTML);
    resolver.setSuffix(".html");
    return resolver;
  }

  public ITemplateResolver javascriptTemplateResolver() {
    SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
    resolver.setApplicationContext(applicationContext);
    resolver.setOrder(1);
    resolver.setCheckExistence(true);
    resolver.setPrefix("classpath:/static/js/");
    resolver.setCacheable(false);
    resolver.setTemplateMode(TemplateMode.JAVASCRIPT);
    resolver.setSuffix(".js");
    return resolver;
  }

  @Bean
  public MessageSource uiMessageSource() {
    ResourceBundleMessageSource msgSource = new ResourceBundleMessageSource();
    msgSource.setAlwaysUseMessageFormat(false);
    msgSource.setBasename("messages");
    msgSource.setDefaultEncoding(CHARACTER_ENCODING);
    msgSource.setFallbackToSystemLocale(true);
    msgSource.setUseCodeAsDefaultMessage(false);
    return msgSource;
  }
}
