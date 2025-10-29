package com.oneshop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    public static final Path PRODUCT_UPLOAD_DIR = Paths.get(System.getProperty("user.dir"), "uploads", "products");
    public static final Path SHOP_UPLOAD_DIR = Paths.get(System.getProperty("user.dir"), "uploads", "shops");

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        String productImagesPath = PRODUCT_UPLOAD_DIR.toUri().toString();
        registry.addResourceHandler("/images/products/**")
                .addResourceLocations(productImagesPath);

    String shopImagesPath = SHOP_UPLOAD_DIR.toUri().toString();
    registry.addResourceHandler("/images/shops/**")
        .addResourceLocations(shopImagesPath);
    }
}
