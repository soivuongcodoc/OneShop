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
}
