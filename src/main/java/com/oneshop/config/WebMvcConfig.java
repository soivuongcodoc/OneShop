package com.oneshop.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    public static final Path PRODUCT_UPLOAD_DIR = Paths.get(System.getProperty("user.dir"), "uploads", "products");
    public static final Path SHOP_UPLOAD_DIR = Paths.get(System.getProperty("user.dir"), "uploads", "shops");

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        String productImagesPath = PRODUCT_UPLOAD_DIR.toUri().toString();
        
        // Map /images/products/** để khớp với database (đường dẫn mới)
        registry.addResourceHandler("/images/products/**")
                .addResourceLocations(productImagesPath);
        
        // Map /uploads/products/** để tương thích với dữ liệu cũ
        registry.addResourceHandler("/uploads/products/**")
                .addResourceLocations(productImagesPath);

        String shopImagesPath = SHOP_UPLOAD_DIR.toUri().toString();
        
        // Map /images/shops/** để khớp với database (đường dẫn mới)
        registry.addResourceHandler("/images/shops/**")
                .addResourceLocations(shopImagesPath);
        
        // Map /uploads/shops/** để tương thích với dữ liệu cũ
        registry.addResourceHandler("/uploads/shops/**")
                .addResourceLocations(shopImagesPath);
    }
}
