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
     * Trang chủ cho Guest - Hiển thị 10 sản phẩm bán chạy nhất hoặc kết quả tìm kiếm
     */
    @GetMapping({"/", "/home"})
    public String homePage(@RequestParam(value = "search", required = false) String search, Model model) {
        List<Product> products;
        
        if (search != null && !search.trim().isEmpty()) {
            // Tìm kiếm sản phẩm theo tên hoặc mô tả
            Page<Product> searchResults = productRepository
                .findByNameContainingIgnoreCase( 
                    search.trim(), 
                    PageRequest.of(0, 100) // Lấy tối đa 100 kết quả
                );
            products = searchResults.getContent();
            model.addAttribute("searchQuery", search.trim());
            model.addAttribute("searchResultCount", products.size());
        } else {
            // Lấy tất cả sản phẩm có sold > 10, sắp xếp từ lớn đến nhỏ
            List<Product> allProducts = productRepository.findBySoldGreaterThanOrderBySoldDesc(10);
            // Chỉ lấy 10 sản phẩm đầu tiên
            products = allProducts.stream().limit(10).toList();
        }
        
        model.addAttribute("products", products);
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

    @GetMapping("/products")
     public String products(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "category", required = false) Long categoryId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        Page<Product> p;
        var pageable = PageRequest.of(page, 8); // 8 sản phẩm / trang

        // Ưu tiên tìm kiếm theo từ khóa
        if (q != null && !q.isBlank()) {
            p = productRepository.findByNameContainingIgnoreCase(q, pageable);
            model.addAttribute("q", q);
        } else if (categoryId != null) { // Sau đó lọc theo category
            p = productRepository.findByCategoryId(categoryId, pageable);
            model.addAttribute("categoryId", categoryId);
            categoryRepository.findById(categoryId).ifPresent(cat -> model.addAttribute("selectedCategory", cat));
        } else { // Mặc định hiển thị tất cả
            p = productRepository.findAll(pageable);
        }

        var categories = categoryRepository.findAll();

        // Luôn trả về các tham số để giữ khi chuyển trang
        model.addAttribute("categories", categories);
        model.addAttribute("products", p.getContent());
        model.addAttribute("totalPages", p.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("pageSize", 8);
        return "user/product";
    }
    
}
