package com.oneshop.controller.vendor;

import com.oneshop.config.WebMvcConfig;
import com.oneshop.dto.vendor.ProductForm;
import com.oneshop.entity.Category;
import com.oneshop.entity.Product;
import com.oneshop.entity.OrderStatus;
import com.oneshop.repository.CategoryRepository;
import com.oneshop.repository.OrderRepository;
import com.oneshop.repository.OrderDetailRepository;
import com.oneshop.repository.ProductRepository;
import com.oneshop.repository.ReviewRepository;
import com.oneshop.security.AuthFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
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
    private final OrderDetailRepository orderDetailRepo;
    private final ReviewRepository reviewRepo;
    private final CategoryRepository categoryRepo;
    private final AuthFacade auth;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Long shopId = auth.requireUserId();
        
        // Tính số liệu thực từ DB
        var allProducts = productRepo.findByShopId(shopId);
        var allOrders = orderRepo.findByShopIdOrderByOrderDateDesc(shopId);
        
        // Đơn hàng hôm nay
        var today = LocalDateTime.now().toLocalDate().atStartOfDay();
        var tomorrow = today.plusDays(1);
        var todayOrders = allOrders.stream()
                .filter(o -> !o.getOrderDate().isBefore(today) && o.getOrderDate().isBefore(tomorrow))
                .toList();
        
        // Đơn chờ xác nhận (PENDING)
        var pendingOrders = allOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.PENDING)
                .toList();
        
        // Doanh thu hôm nay (chỉ đơn CONFIRMED)
        var todayRevenue = todayOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.CONFIRMED)
                .map(o -> o.getTotalAmount())
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        
        model.addAttribute("pageTitle", "Bảng điều khiển - OneShop Vendor");
        model.addAttribute("activePage", "dashboard");
        model.addAttribute("productCount", allProducts.size());
        model.addAttribute("todayOrdersCount", todayOrders.size());
        model.addAttribute("pendingOrdersCount", pendingOrders.size());
        model.addAttribute("todayRevenue", todayRevenue);
        return "vendor/dashboard";
    }

    @GetMapping("/orders")
    public String orders(@RequestParam(required = false) OrderStatus status, Model model) {
        Long shopId = auth.requireUserId();
        var orders = (status == null) 
            ? orderRepo.findByShopIdOrderByOrderDateDesc(shopId)
            : orderRepo.findByShopIdAndStatusOrderByOrderDateDesc(shopId, status);
        
        model.addAttribute("pageTitle", "Quản lý đơn hàng - OneShop Vendor");
        model.addAttribute("activePage", "orders");
        model.addAttribute("orders", orders);
        model.addAttribute("currentStatus", status);
        return "vendor/orders";
    }

    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable Long id, Model model) {
        Long shopId = auth.requireUserId();
        var order = orderRepo.findByIdAndShopId(id, shopId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
        var orderDetails = orderDetailRepo.findByOrderId(id);
        
        model.addAttribute("pageTitle", "Chi tiết đơn hàng - OneShop Vendor");
        model.addAttribute("activePage", "orders");
        model.addAttribute("order", order);
        model.addAttribute("orderDetails", orderDetails);
        model.addAttribute("statuses", OrderStatus.values());
        return "vendor/order-detail";
    }

    @PostMapping("/orders/{id}/status")
    public String updateOrderStatus(@PathVariable Long id, @RequestParam("status") OrderStatus status) {
        Long shopId = auth.requireUserId();
        var order = orderRepo.findByIdAndShopId(id, shopId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
        order.setStatus(status);
        orderRepo.save(order);
        return "redirect:/vendor/orders/" + id;
    }

    @PostMapping("/orders/{id}/confirm")
    public String confirmOrder(@PathVariable Long id) {
        Long shopId = auth.requireUserId();
        var order = orderRepo.findByIdAndShopId(id, shopId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
        if (order.getStatus() == OrderStatus.PENDING) {
            order.setStatus(OrderStatus.CONFIRMED);
            orderRepo.save(order);
        }
        return "redirect:/vendor/orders/" + id;
    }

    @PostMapping("/orders/{id}/cancel")
    public String cancelOrder(@PathVariable Long id) {
        Long shopId = auth.requireUserId();
        var order = orderRepo.findByIdAndShopId(id, shopId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
        if (order.getStatus() != OrderStatus.CANCELLED) {
            order.setStatus(OrderStatus.CANCELLED);
            orderRepo.save(order);
        }
        return "redirect:/vendor/orders/" + id;
    }

    @GetMapping("/products")
    public String products(Model model) {
        Long shopId = auth.requireUserId();
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

        Long shopId = auth.requireUserId();
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
        Long shopId = auth.requireUserId();
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
        Long shopId = auth.requireUserId();
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
            String originalFilename = image.getOriginalFilename();
            String ext = (originalFilename != null && originalFilename.contains("."))
                    ? originalFilename.substring(originalFilename.lastIndexOf('.')) : "";
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
        Long shopId = auth.requireUserId();
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
    public String revenue(@RequestParam(required = false) String start,
                          @RequestParam(required = false) String end,
                          Model model) {
        model.addAttribute("pageTitle", "Thống kê doanh thu - OneShop Vendor");
        model.addAttribute("activePage", "revenue");

        Long shopId = auth.requireUserId();
        java.time.LocalDate startDate = (start != null && !start.isBlank()) ? java.time.LocalDate.parse(start) : java.time.LocalDate.now().minusDays(7);
        java.time.LocalDate endDate = (end != null && !end.isBlank()) ? java.time.LocalDate.parse(end) : java.time.LocalDate.now();
        var startDt = startDate.atStartOfDay();
        var endDt = endDate.atTime(23,59,59);

        // simple in-memory aggregation using existing repo methods
        var all = orderRepo.findByShopIdOrderByOrderDateDesc(shopId).stream()
                .filter(o -> !o.getOrderDate().isBefore(startDt) && !o.getOrderDate().isAfter(endDt))
                .toList();
        var confirmed = all.stream().filter(o -> o.getStatus() == com.oneshop.entity.OrderStatus.CONFIRMED).toList();
        java.math.BigDecimal totalRevenue = confirmed.stream()
                .map(com.oneshop.entity.Order::getTotalAmount)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        model.addAttribute("start", startDate);
        model.addAttribute("end", endDate);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("totalOrders", all.size());
        model.addAttribute("confirmedOrders", confirmed.size());
        model.addAttribute("orders", all);
        return "vendor/revenue";
    }
}
