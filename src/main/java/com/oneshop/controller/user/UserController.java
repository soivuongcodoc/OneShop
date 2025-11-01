package com.oneshop.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oneshop.entity.Order;
import com.oneshop.entity.OrderStatus;
import com.oneshop.entity.Product;
import com.oneshop.entity.User;
import com.oneshop.entity.Customer;
import com.oneshop.repository.CartRepository;
import com.oneshop.repository.OrderRepository;
import com.oneshop.repository.ProductRepository;
import com.oneshop.repository.ReviewRepository;
import com.oneshop.repository.ShopRepository;
import com.oneshop.repository.ViewedProductRepository;
import com.oneshop.repository.WishlistRepository;
import com.oneshop.repository.CustomerRepository;
import com.oneshop.service.user.UserService;
import com.oneshop.service.NotificationService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private ViewedProductRepository viewedProductRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private com.oneshop.repository.OrderDetailRepository orderDetailRepository;
    
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CustomerRepository customerRepository;

    // 📊 Dashboard User - Trang tổng quan
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }

        Long userId = currentUser.getId();

        // Đếm số lượng đơn hàng theo trạng thái
        List<Order> allOrders = orderRepository.findByCustomer_User_IdOrderByOrderDateDesc(userId);
        long totalOrders = allOrders.size();
        long pendingOrders = allOrders.stream().filter(o -> o.getStatus() == OrderStatus.PENDING).count();
        long confirmedOrders = allOrders.stream().filter(o -> o.getStatus() == OrderStatus.CONFIRMED).count();
        long cancelledOrders = allOrders.stream().filter(o -> o.getStatus() == OrderStatus.CANCELLED).count();

        // Đếm sản phẩm yêu thích
        long wishlistCount = wishlistRepository.findByUserId(userId).size();

        // Đếm sản phẩm đã xem
        long viewedCount = viewedProductRepository.findByUserIdOrderByViewedAtDesc(userId).size();

        // Đếm sản phẩm trong giỏ hàng
        var cart = cartRepository.findByUserId(userId);
        long cartItemsCount = cart.map(c -> c.getItems() != null ? c.getItems().size() : 0).orElse(0);

        // Đơn hàng gần đây (5 đơn mới nhất)
        var recentOrders = allOrders.stream().limit(5).toList();

        // Sản phẩm yêu thích gần nhất
        var recentWishlist = wishlistRepository.findByUserId(userId).stream().limit(4).toList();

        // Sản phẩm đã xem gần nhất
        var recentViewed = viewedProductRepository.findByUserIdOrderByViewedAtDesc(userId).stream().limit(4).toList();

        model.addAttribute("user", currentUser);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("pendingOrders", pendingOrders);
        model.addAttribute("confirmedOrders", confirmedOrders);
        model.addAttribute("cancelledOrders", cancelledOrders);
        model.addAttribute("wishlistCount", wishlistCount);
        model.addAttribute("viewedCount", viewedCount);
        model.addAttribute("cartItemsCount", cartItemsCount);
        model.addAttribute("recentOrders", recentOrders);
        model.addAttribute("recentWishlist", recentWishlist);
        model.addAttribute("recentViewed", recentViewed);
        
        // Check if user has VENDOR role
        boolean hasVendorRole = currentUser.getRoles().stream()
                .anyMatch(role -> "ROLE_VENDOR".equals(role.getName()));
        model.addAttribute("hasVendorRole", hasVendorRole);

        return "user/dashboard";
    }

    // 🏆 Top 20 Sản Phẩm - Hiển thị theo tab với phân trang
    @GetMapping("/top20")
    public String top20(
            @RequestParam(value = "type", defaultValue = "new") String type,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        List<Product> productsList;
        int totalPages = 1;

        // Lấy 20 sản phẩm theo loại
        switch (type) {
            case "bestselling":
                Page<Product> bestSelling = productRepository.findAllByOrderBySoldDesc(PageRequest.of(page, 20));
                productsList = bestSelling.getContent();
                totalPages = bestSelling.getTotalPages();
                model.addAttribute("activeTab", "bestselling");
                model.addAttribute("title", "Sản phẩm bán chạy");
                break;
            case "toprated":
                Page<Product> topRated = productRepository.findTopRated(PageRequest.of(page, 20));
                productsList = topRated.getContent();
                totalPages = topRated.getTotalPages();
                model.addAttribute("activeTab", "toprated");
                model.addAttribute("title", "Sản phẩm đánh giá cao");
                break;
            case "mostfavorited":
                // Dùng method mới trả về List - chỉ lấy top 20, không phân trang
                productsList = productRepository.findTop20MostFavorited();
                totalPages = 1; // Chỉ 1 trang vì lấy top 20
                model.addAttribute("activeTab", "mostfavorited");
                model.addAttribute("title", "Sản phẩm yêu thích");
                break;
            default: // new
                Page<Product> newest = productRepository.findAllByOrderByIdDesc(PageRequest.of(page, 20));
                productsList = newest.getContent();
                totalPages = newest.getTotalPages();
                model.addAttribute("activeTab", "new");
                model.addAttribute("title", "Sản phẩm mới");
                break;
        }

        model.addAttribute("products", productsList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("type", type);

        return "user/top20";
    }

    @Autowired
    private com.oneshop.repository.CategoryRepository categoryRepository;

    // 📦 Danh sách sản phẩm (guest + user) - hỗ trợ lọc theo danh mục
    @GetMapping("products")
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

    // 📄 Chi tiết sản phẩm
    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable("id") Long id, 
                                @RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "success", required = false) String success,
                                Model model) {
        var opt = productRepository.findById(id);
        if (opt.isEmpty()) {
            return "redirect:/user/products";
        }

        Product product = opt.get();
        
        // Load shop info if product has shopId
        if (product.getShopId() != null) {
            shopRepository.findById(product.getShopId()).ifPresent(shop -> {
                model.addAttribute("shop", shop);
            });
        }

        // Load reviews
        var reviews = reviewRepository.findByProductIdOrderByCreatedAtDesc(id);
        
        // Check if current user has purchased this product
        var currentUser = userService.getCurrentUser();
        boolean hasPurchased = false;
        if (currentUser != null) {
            hasPurchased = orderDetailRepository.existsPurchasedByUserAndProduct(currentUser.getId(), id);
        }

        model.addAttribute("product", product);
        model.addAttribute("reviews", reviews);
        model.addAttribute("hasPurchased", hasPurchased);
        model.addAttribute("error", error);
        model.addAttribute("success", success);
        return "user/product-detail";
    }

    @Autowired
    private com.oneshop.repository.AddressRepository addressRepository;

    // 👤 Trang thông tin tài khoản (hiển thị thông tin user + địa chỉ)
    @GetMapping("/profile")
    public String profile(Model model) {
        User u = userService.getCurrentUser();
        if (u == null) {
            return "redirect:/login";
        }
        // Load Customer info
        var customer = customerRepository.findByUserId(u.getId());
        
        // Load danh sách địa chỉ
        var addresses = addressRepository.findByUserId(u.getId());

        model.addAttribute("user", u);
        model.addAttribute("customer", customer.orElse(null));
        model.addAttribute("addresses", addresses);
        return "user/profile";
    }

    // ✏️ Cập nhật thông tin người dùng
    @PostMapping("/profile/update")
    public String updateProfile(
            @RequestParam("email") String email,
            @RequestParam("fullName") String fullName,
            @RequestParam("phone") String phone) {
        User current = userService.getCurrentUser();
        if (current == null) {
            return "redirect:/auth/login";
        }

        // Update User email
        current.setEmail(email);
        userService.updateProfile(current);

        // Update Customer fullName and phone
        var customer = customerRepository.findByUserId(current.getId());
        if (customer.isPresent()) {
            Customer c = customer.get();
            c.setFullName(fullName);
            c.setPhone(phone);
            customerRepository.save(c);
        } else {
            // Create new Customer if not exists
            Customer newCustomer = Customer.builder()
                    .user(current)
                    .fullName(fullName)
                    .phone(phone)
                    .build();
            customerRepository.save(newCustomer);
        }

        return "redirect:/user/profile";
    }
}
