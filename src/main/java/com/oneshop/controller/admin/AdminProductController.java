package com.oneshop.controller.admin;

import com.oneshop.config.WebMvcConfig;
import com.oneshop.dto.vendor.ProductForm;
import com.oneshop.entity.Category;
import com.oneshop.entity.Product;
import com.oneshop.entity.Shop;
import com.oneshop.repository.CategoryRepository;
import com.oneshop.repository.AdminProductRepository;
import com.oneshop.repository.ShopRepository;
import com.oneshop.service.admin.ProductService;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final AdminProductRepository productRepo;
    private final CategoryRepository categoryRepo;
    @Autowired
    private ShopRepository shopRepo;
    @Autowired
    private ProductService productService;
    @Autowired
    private com.oneshop.service.NotificationService notificationService;
    @Autowired
    private EntityManager entityManager;

   @GetMapping
    public String listProducts(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Product> products = (keyword == null || keyword.isBlank())
                ? productRepo.findAllByOrderByIdDesc()
                : productRepo.findByNameContainingIgnoreCaseOrderByIdDesc(keyword);
        
        // Tạo một Map để tra cứu tên shop hiệu quả hơn trong view
        Map<Long, String> shopNames = shopRepo.findAll().stream()
                .collect(Collectors.toMap(Shop::getId, Shop::getName));

        model.addAttribute("products", products);
        model.addAttribute("shopNames", shopNames); // Truyền Map tên shop sang view
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageTitle", "Quản lý sản phẩm");
        return "admin/products";
    }


    @GetMapping("/new")
    public String newProduct(Model model) {
        model.addAttribute("pageTitle", "Thêm sản phẩm - Admin");
        model.addAttribute("activePage", "products");
        model.addAttribute("form", new ProductForm());
        model.addAttribute("categories", categoryRepo.findAll());
        model.addAttribute("shops", shopRepo.findAll()); // Admin cần chọn shop
        model.addAttribute("mode", "create"); // Thêm mode để form biết là tạo mới
        model.addAttribute("productId", null); // Thêm productId=null để template không bị lỗi
        return "admin/product-form";
    }

    @PostMapping
    public String createProduct(@Valid @ModelAttribute("form") ProductForm form,
                                BindingResult binding, Model model) throws IOException {
        if (binding.hasErrors()) {
            model.addAttribute("pageTitle", "Thêm sản phẩm - Admin");
            model.addAttribute("activePage", "products");
            model.addAttribute("categories", categoryRepo.findAll());
            model.addAttribute("shops", shopRepo.findAll());
            model.addAttribute("mode", "create");
            model.addAttribute("productId", null);
            return "admin/product-form";
        }

        Category category = categoryRepo.findById(form.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));

        String imageUrl = null;
        MultipartFile image = form.getImage();
        if (image != null && !image.isEmpty()) {
            Files.createDirectories(WebMvcConfig.PRODUCT_UPLOAD_DIR);
            String originalFilename = image.getOriginalFilename();
            String ext = (originalFilename != null && originalFilename.contains("."))
                    ? originalFilename.substring(originalFilename.lastIndexOf('.')) : "";
            String filename = UUID.randomUUID() + ext;
            Path target = WebMvcConfig.PRODUCT_UPLOAD_DIR.resolve(filename);
            image.transferTo(target.toFile());
            imageUrl = "/images/products/" + filename;
        }

        Product p = Product.builder()
                .name(form.getName())
                .description(form.getDescription())
                .price(form.getPrice())
                .stock(form.getStock())
                .imageUrl(imageUrl)
                .category(category)
                .shopId(form.getShopId())
                .build();
        productRepo.save(p);
        return "redirect:/admin/products";
    }

    @GetMapping("/{id}/edit")
    public String editProduct(@PathVariable Long id, Model model) {
        var p = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        ProductForm form = new ProductForm();
        if (p.getCategory() != null) {
            form.setCategoryId(p.getCategory().getId());
        }
        form.setName(p.getName());
        form.setDescription(p.getDescription());
        form.setPrice(p.getPrice());
        form.setStock(p.getStock());
        form.setActive(p.getActive());
        form.setShopId(p.getShopId()); // Gán shopId hiện tại vào form

        model.addAttribute("pageTitle", "Sửa sản phẩm - Admin");
        model.addAttribute("activePage", "products");
        model.addAttribute("form", form);
        model.addAttribute("categories", categoryRepo.findAll());
        model.addAttribute("shops", shopRepo.findAll()); // Admin có thể đổi shop
        model.addAttribute("mode", "edit"); // Thêm mode để form biết là chỉnh sửa
        model.addAttribute("productId", id);
        model.addAttribute("currentImage", p.getImageUrl());
        return "admin/product-form";
    }

    @PostMapping("/{id}")
    public String updateProduct(@PathVariable Long id,
                                @Valid @ModelAttribute("form") ProductForm form,
                                BindingResult binding, Model model) throws IOException {
        var p = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        if (binding.hasErrors()) {
            model.addAttribute("pageTitle", "Sửa sản phẩm - Admin");
            model.addAttribute("activePage", "products");
            model.addAttribute("categories", categoryRepo.findAll());
            model.addAttribute("shops", shopRepo.findAll());
            model.addAttribute("productId", id);
            model.addAttribute("mode", "edit");
            model.addAttribute("currentImage", p.getImageUrl());
            return "admin/product-form";
        }

        Category category = categoryRepo.findById(form.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));

        MultipartFile image = form.getImage();
        if (image != null && !image.isEmpty()) {
            Files.createDirectories(WebMvcConfig.PRODUCT_UPLOAD_DIR);
            String originalFilename = image.getOriginalFilename();
            String ext = (originalFilename != null && originalFilename.contains("."))
                    ? originalFilename.substring(originalFilename.lastIndexOf('.')) : "";
            String filename = UUID.randomUUID() + ext;
            Path target = WebMvcConfig.PRODUCT_UPLOAD_DIR.resolve(filename);
            image.transferTo(target.toFile());
            p.setImageUrl("/images/products/" + filename);
        }

        p.setShopId(form.getShopId()); // Cho phép admin thay đổi shop
        p.setCategory(category);
        p.setName(form.getName());
        p.setDescription(form.getDescription());
        p.setPrice(form.getPrice());
        p.setStock(form.getStock());
        p.setActive(form.getActive() != null ? form.getActive() : Boolean.TRUE);
        productRepo.save(p);
        return "redirect:/admin/products";
    }

    @PostMapping("/{id}/delete")
    @Transactional
    public String deleteProduct(@PathVariable Long id) {
        var p = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
        
        // Gửi thông báo cho vendor nếu sản phẩm thuộc về shop
        if (p.getShopId() != null) {
            shopRepo.findById(p.getShopId()).ifPresent(shop -> {
                notificationService.createProductDeletedNotification(
                    shop.getVendor().getId(),
                    p.getId(),
                    p.getName()
                );
            });
        }
        
        // Xóa tất cả cart items liên quan đến sản phẩm này
        entityManager.createQuery("DELETE FROM CartItemEntity c WHERE c.product.id = :productId")
                .setParameter("productId", id)
                .executeUpdate();
        
        // Xóa ảnh nếu có
        if (p.getImageUrl() != null && p.getImageUrl().startsWith("/images/products/")) {
            String old = p.getImageUrl().substring("/images/products/".length());
            try { Files.deleteIfExists(WebMvcConfig.PRODUCT_UPLOAD_DIR.resolve(old)); } catch (Exception ignored) {}
        }
        productRepo.delete(p);
        return "redirect:/admin/products";
    }
    
}