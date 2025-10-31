package com.oneshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DecoratorController {

    // SiteMesh sẽ forward nội bộ tới URL này để lấy layout
    @GetMapping("/decorators/main")
    public String mainDecorator() {
        // Trả về view Thymeleaf: /templates/decorators/main.html
        return "decorators/main";
    }

    
    @GetMapping("/decorators/vendor-layout")
    public String vendorLayoutDecorator() {
        // Trả về view Thymeleaf: /templates/decorators/vendor-layout.html
        return "decorators/vendor-layout";
    }

    @GetMapping("/decorators/admin-layout")
    public String adminLayoutDecorator() {
        // Trả về view Thymeleaf: /templates/decorators/admin-layout.html
        return "decorators/admin-layout";
    }

    @GetMapping("/decorators/user-layout")
    public String userLayoutDecorator() {
        // Trả về view Thymeleaf: /templates/decorators/user-layout.html
        return "decorators/user-layout";
    }

}
