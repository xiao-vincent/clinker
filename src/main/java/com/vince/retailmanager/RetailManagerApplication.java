package com.vince.retailmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;

@SpringBootApplication
public class RetailManagerApplication implements WebMvcConfigurer {

  public static void main(String[] args) {
    SpringApplication.run(RetailManagerApplication.class, args);
  }

  @Override
  public void configurePathMatch(PathMatchConfigurer configurer) {
    UrlPathHelper urlPathHelper = new UrlPathHelper();
    urlPathHelper.setRemoveSemicolonContent(false);
    configurer.setUrlPathHelper(urlPathHelper);
  }

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(new YearMonthConverter());
    registry.addConverter(new DistributionTypeConverter());


  }

}
