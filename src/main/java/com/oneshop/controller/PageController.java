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
     * Trang danh sách sản phẩm cho Guest - hỗ trợ lọc theo danh mục
     */
    @GetMapping("/products")
    public String productsPage(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "category", required = false) Long categoryId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        Page<Product> p;

        // Ưu tiên tìm kiếm theo từ khóa
        if (q != null && !q.isBlank()) {
            p = productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                    q, q, PageRequest.of(page, 20)
            );
            model.addAttribute("q", q);
        } // Sau đó lọc theo category
        else if (categoryId != null) {
            p = productRepository.findByCategoryId(categoryId, PageRequest.of(page, 20));
            model.addAttribute("categoryId", categoryId);
            // Lấy tên category
            categoryRepository.findById(categoryId).ifPresent(cat
                    -> model.addAttribute("selectedCategory", cat)
            );
        } // Mặc định hiển thị tất cả (tăng số lượng)
        else {
            p = productRepository.findAll(PageRequest.of(page, 50));
        }

        // Lấy danh sách categories để hiển thị
        var categories = categoryRepository.findAll();

        model.addAttribute("categories", categories);
        model.addAttribute("products", p.getContent());
        model.addAttribute("totalPages", p.getTotalPages());
        model.addAttribute("currentPage", page);
        return "product"; // templates/product.html
    }

    /**
     * Trang chi tiết sản phẩm cho Guest
     */
    @GetMapping("/product/{id}")
    public String productDetailPage(@org.springframework.web.bind.annotation.PathVariable Long id, Model model) {
        var product = productRepository.findById(id);
        if (product.isEmpty()) {
            return "redirect:/products";
        }

        // Load reviews
        var reviews = reviewRepository.findByProductIdOrderByCreatedAtDesc(id);

        model.addAttribute("product", product.get());
        model.addAttribute("reviews", reviews);
        return "product-detail"; // templates/product-detail.html
    }

}
