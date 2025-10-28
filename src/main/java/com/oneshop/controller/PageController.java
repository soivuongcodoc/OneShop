package com.oneshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    // Trang test
    @GetMapping("/test")
    public String testPage() {
        return "test";
    }
    @GetMapping("/admin/dashboard")
    public String dashboard() {
        return "admin/dashboard"; // Tạo file dashboard.html
    }
    // Trang đăng nhập
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login"; // file: templates/auth/login.html
    }
    @GetMapping("/vendor/home")
    public String home() {
        return "vendor/home"; // Tạo file home.html
    }
    // Trang đăng ký
    @GetMapping("/register")
    public String registerPage() {
        return "auth/register"; // file: templates/auth/register.html
    }

    // Trang quên mật khẩu
    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "auth/forgot-password"; // file: templates/auth/forgot-password.html
    }

    // Trang xác thực email (OTP)
    @GetMapping("/verify")
    public String verifyEmailPage() {
        return "auth/verify"; // file: templates/auth/verify-email.html
    }
    @GetMapping("/")
    public String indexPage() {
        return "index"; // trả về file index.html trong templates
    }

    // Trang đặt lại mật khẩu
    @GetMapping("/reset-password")
    public String resetPasswordPage() {
        return "auth/reset-password"; // file: templates/auth/reset-password.html
    }
    @GetMapping("/home")
    public String homePage() {
        return "home";
    }

}
