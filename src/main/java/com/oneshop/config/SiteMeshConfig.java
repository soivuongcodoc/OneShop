package com.oneshop.config;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SiteMeshConfig {

    @Bean
    public FilterRegistrationBean<ConfigurableSiteMeshFilter> siteMeshFilter() {
        FilterRegistrationBean<ConfigurableSiteMeshFilter> filter = new FilterRegistrationBean<>();

        filter.setFilter(new ConfigurableSiteMeshFilter() {
            @Override
            protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
                builder
                        // Vendor pages dùng vendor layout
                        .addDecoratorPath("/vendor/*", "/decorators/vendor-layout")
                        // Admin pages dùng admin layout riêng
                        .addDecoratorPath("/admin/*", "/decorators/admin-layout")
                        // User pages dùng user layout riêng
                        .addDecoratorPath("/user/*", "/decorators/user-layout")
                        // Trang products dùng user layout
                        .addDecoratorPath("/products", "/decorators/user-layout")
                        // Các trang công khai dùng main layout
                        .addDecoratorPath("/login", "/decorators/main")
                        .addDecoratorPath("/register", "/decorators/main")
                        .addDecoratorPath("/", "/decorators/main")
                        // tránh chính layout bị decor lần nữa
                        .addExcludedPath("/decorators/*")
                        .addExcludedPath("/layout/*")
                        // bỏ qua tài nguyên tĩnh
                        .addExcludedPath("/css/*")
                        .addExcludedPath("/js/*")
                        .addExcludedPath("/images/*")
                        .addExcludedPath("/uploads/*");
            }
        });

        filter.addUrlPatterns("/*");
        filter.setOrder(Integer.MIN_VALUE);
        return filter;
    }
}
