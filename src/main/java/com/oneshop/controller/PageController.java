package com.oneshop.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oneshop.entity.Product;
import com.oneshop.repository.CategoryRepository;
import com.oneshop.repository.ProductRepository;
import com.oneshop.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ReviewRepository reviewRepository;

    /**
     * Trang chủ cho Guest - Hiển thị 10 sản phẩm bán chạy nhất
     */
    @GetMapping({"/", "/home"})
    public String homePage(Model model) {
        // Lấy tất cả sản phẩm có sold > 10, sắp xếp từ lớn đến nhỏ
        List<Product> allProducts = productRepository.findBySoldGreaterThanOrderBySoldDesc(10);
        // Chỉ lấy 10 sản phẩm đầu tiên
        List<Product> top10Products = allProducts.stream().limit(10).toList();
        model.addAttribute("products", top10Products);
        return "home"; // trả về templates/home.html
    }

    // Trang đăng nhập
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login"; // file: templates/auth/login.html
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

    // Trang đặt lại mật khẩu
    @GetMapping("/reset-password")
    public String resetPasswordPage() {
        return "auth/reset-password"; // file: templates/auth/reset-password.html
    }

    /**
     * Redirect /products to home page
     */
    @GetMapping("/products")
    public String productsPage() {
        return "redirect:/";
    }

}
