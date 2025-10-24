package com.oneshop.controller;

import com.oneshop.config.WebMvcConfig;
import com.oneshop.dto.ProductForm;
import com.oneshop.entity.Category;
import com.oneshop.entity.Product;
import com.oneshop.repository.CategoryRepository;
import com.oneshop.repository.OrderRepository;
import com.oneshop.repository.ProductRepository;
import com.oneshop.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/vendor")
@RequiredArgsConstructor
public class VendorController {
    
    private final ProductRepository productRepo;
    private final OrderRepository orderRepo;
        private final ReviewRepository reviewRepo;
    private final CategoryRepository categoryRepo;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("pageTitle", "Dashboard - OneShop Vendor");
        model.addAttribute("activePage", "dashboard");
        model.addAttribute("productCount", 125);
        model.addAttribute("todayOrders", 8);
        model.addAttribute("shippingOrders", 14);
        model.addAttribute("todayRevenue", "5.200.000");
        model.addAttribute("vendorName", "Cửa hàng của tôi");
        return "vendor/dashboard";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        // TODO: Get shopId from authenticated user
        Long shopId = 1L; // Demo
        var orders = orderRepo.findByShopIdOrderByOrderDateDesc(shopId);
        
        model.addAttribute("pageTitle", "Quản lý đơn hàng - OneShop Vendor");
        model.addAttribute("activePage", "orders");
        model.addAttribute("orders", orders);
        return "vendor/orders";
    }

    @GetMapping("/products")
    public String products(Model model) {
        // TODO: Get shopId from authenticated user
        Long shopId = 1L; // Demo
        var products = productRepo.findByShopId(shopId);
        
        model.addAttribute("pageTitle", "Quản lý sản phẩm - OneShop Vendor");
        model.addAttribute("activePage", "products");
        model.addAttribute("products", products);
        return "vendor/product";
    }

    // ====== Create product ======
    @GetMapping("/products/new")
    public String newProduct(Model model) {
        model.addAttribute("pageTitle", "Thêm sản phẩm - OneShop Vendor");
        model.addAttribute("activePage", "products");
        model.addAttribute("form", new ProductForm());
        model.addAttribute("categories", categoryRepo.findAll());
        model.addAttribute("mode", "create");
        return "vendor/product-form";
    }

    @PostMapping("/products")
    public String createProduct(@Valid @ModelAttribute("form") ProductForm form,
                                BindingResult binding, Model model) throws IOException {
        if (binding.hasErrors()) {
            model.addAttribute("categories", categoryRepo.findAll());
            model.addAttribute("mode", "create");
            return "vendor/product-form";
        }

        Long shopId = 1L; // TODO: from auth
        Category category = categoryRepo.findById(form.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));

        String imageUrl = null;
        MultipartFile image = form.getImage();
        if (image != null && !image.isEmpty()) {
            Files.createDirectories(WebMvcConfig.PRODUCT_UPLOAD_DIR);
            String ext = image.getOriginalFilename() != null && image.getOriginalFilename().contains(".")
                    ? image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf('.')) : "";
            String filename = UUID.randomUUID() + ext;
            Path target = WebMvcConfig.PRODUCT_UPLOAD_DIR.resolve(filename);
            image.transferTo(target.toFile());
            imageUrl = "/images/products/" + filename;
        }

        Product p = Product.builder()
                .shopId(shopId)
                .category(category)
                .name(form.getName())
                .description(form.getDescription())
                .price(form.getPrice())
                .stock(form.getStock())
                .active(form.getActive() != null ? form.getActive() : Boolean.TRUE)
                .imageUrl(imageUrl)
                .createdAt(LocalDateTime.now())
                .build();
        productRepo.save(p);
        return "redirect:/vendor/products";
    }

    // ====== Edit product ======
    @GetMapping("/products/{id}/edit")
    public String editProduct(@PathVariable Long id, Model model) {
        Long shopId = 1L; // TODO from auth
        var p = productRepo.findByIdAndShopId(id, shopId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
        ProductForm form = new ProductForm();
        form.setCategoryId(p.getCategory().getId());
        form.setName(p.getName());
        form.setDescription(p.getDescription());
        form.setPrice(p.getPrice());
        form.setStock(p.getStock());
        form.setActive(p.getActive());

        model.addAttribute("pageTitle", "Sửa sản phẩm - OneShop Vendor");
        model.addAttribute("activePage", "products");
        model.addAttribute("form", form);
        model.addAttribute("categories", categoryRepo.findAll());
        model.addAttribute("mode", "edit");
        model.addAttribute("productId", id);
        model.addAttribute("currentImage", p.getImageUrl());
        return "vendor/product-form";
    }

    @PostMapping("/products/{id}")
    public String updateProduct(@PathVariable Long id,
                                @Valid @ModelAttribute("form") ProductForm form,
                                BindingResult binding, Model model) throws IOException {
        Long shopId = 1L; // TODO from auth
        var p = productRepo.findByIdAndShopId(id, shopId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        if (binding.hasErrors()) {
            model.addAttribute("categories", categoryRepo.findAll());
            model.addAttribute("mode", "edit");
            model.addAttribute("productId", id);
            model.addAttribute("currentImage", p.getImageUrl());
            return "vendor/product-form";
        }

        Category category = categoryRepo.findById(form.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));

        // handle image
        MultipartFile image = form.getImage();
        if (image != null && !image.isEmpty()) {
            Files.createDirectories(WebMvcConfig.PRODUCT_UPLOAD_DIR);
            String ext = image.getOriginalFilename() != null && image.getOriginalFilename().contains(".")
                    ? image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf('.')) : "";
            String filename = UUID.randomUUID() + ext;
            Path target = WebMvcConfig.PRODUCT_UPLOAD_DIR.resolve(filename);
            image.transferTo(target.toFile());
            // delete old file if existed and it's our images mapping
            if (p.getImageUrl() != null && p.getImageUrl().startsWith("/images/products/")) {
                String old = p.getImageUrl().substring("/images/products/".length());
                try { Files.deleteIfExists(WebMvcConfig.PRODUCT_UPLOAD_DIR.resolve(old)); } catch (Exception ignored) {}
            }
            p.setImageUrl("/images/products/" + filename);
        }

        p.setCategory(category);
        p.setName(form.getName());
        p.setDescription(form.getDescription());
        p.setPrice(form.getPrice());
        p.setStock(form.getStock());
        p.setActive(form.getActive() != null ? form.getActive() : Boolean.TRUE);
        productRepo.save(p);
        return "redirect:/vendor/products";
    }

    // ====== Delete product ======
    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        Long shopId = 1L; // TODO from auth
        var p = productRepo.findByIdAndShopId(id, shopId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
        if (p.getImageUrl() != null && p.getImageUrl().startsWith("/images/products/")) {
            String old = p.getImageUrl().substring("/images/products/".length());
            try { Files.deleteIfExists(WebMvcConfig.PRODUCT_UPLOAD_DIR.resolve(old)); } catch (Exception ignored) {}
        }
        productRepo.delete(p);
        return "redirect:/vendor/products";
    }

    @GetMapping("/home")
    public String vendorHome(Model model) {
        model.addAttribute("pageTitle", "Vendor Home - OneShop");
        model.addAttribute("activePage", "dashboard");
        return "vendor/home";
    }

        @GetMapping("/products/{id}")
        public String productDetail(@PathVariable Long id, Model model) {
            var product = productRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
            var reviews = reviewRepo.findByProductId(id);
        
            model.addAttribute("pageTitle", "Chi tiết sản phẩm - OneShop Vendor");
            model.addAttribute("activePage", "products");
            model.addAttribute("product", product);
            model.addAttribute("reviews", reviews);
            return "vendor/product-detail";
        }

    @GetMapping("/revenue")
    public String revenue(Model model) {
        model.addAttribute("pageTitle", "Thống kê doanh thu - OneShop Vendor");
        model.addAttribute("activePage", "revenue");
        model.addAttribute("vendorName", "Cửa hàng của tôi");
        model.addAttribute("totalRevenue", "12.500.000");
        return "vendor/revenue";
    }
}