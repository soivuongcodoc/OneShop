package com.oneshop.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;

@Configuration
public class SiteMeshConfig {

  @Bean
  public FilterRegistrationBean<ConfigurableSiteMeshFilter> siteMeshFilter() {
    FilterRegistrationBean<ConfigurableSiteMeshFilter> filter = new FilterRegistrationBean<>();

    filter.setFilter(new ConfigurableSiteMeshFilter() {
      @Override
      protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
        builder
          .addDecoratorPath("/login", "/decorators/main")
          .addDecoratorPath("/register", "/decorators/main")
          .addDecoratorPath("/test", "/decorators/main")
          .addDecoratorPath("/", "/decorators/main")
          .addDecoratorPath("/dashboard", "/decorators/main")
          .addDecoratorPath("/vendor/home", "/decorators/main")
          // tránh chính layout bị decor lần nữa
          .addExcludedPath("/decorators/*")
          // bỏ qua tài nguyên tĩnh
          .addExcludedPath("/css/*")
          .addExcludedPath("/js/*")
          .addExcludedPath("/images/*");
      }
    });

    filter.addUrlPatterns("/*");
    filter.setOrder(Integer.MIN_VALUE);
    return filter;
  }
}
