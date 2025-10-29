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
import com.oneshop.repository.CartRepository;
import com.oneshop.repository.OrderRepository;
import com.oneshop.repository.ProductRepository;
import com.oneshop.repository.ReviewRepository;
import com.oneshop.repository.ViewedProductRepository;
import com.oneshop.repository.WishlistRepository;
import com.oneshop.service.user.UserService;

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
    private CartRepository cartRepository;

    @Autowired
    private com.oneshop.repository.OrderDetailRepository orderDetailRepository;

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
        long shippingOrders = allOrders.stream().filter(o -> o.getStatus() == OrderStatus.SHIPPING).count();
        long deliveredOrders = allOrders.stream().filter(o -> o.getStatus() == OrderStatus.DELIVERED).count();
        long cancelledOrders = allOrders.stream().filter(o -> o.getStatus() == OrderStatus.CANCELLED).count();

        // Đếm sản phẩm yêu thích
        long wishlistCount = wishlistRepository.findByUserId(userId).size();

        // Đếm sản phẩm đã xem
        long viewedCount = viewedProductRepository.findByUserIdOrderByViewedAtDesc(userId).size();

        // Đếm sản phẩm trong giỏ hàng
        var cart = cartRepository.findByUserId(userId);
        long cartItemsCount = cart.map(c -> c.getItems() != null ? c.getItems().size() : 0).orElse(0);

        // Đơn hàng gần nhất
        List<Order> recentOrders = allOrders.stream().limit(5).toList();

        // Sản phẩm yêu thích gần nhất
        var recentWishlist = wishlistRepository.findByUserId(userId).stream().limit(4).toList();

        // Sản phẩm đã xem gần nhất
        var recentViewed = viewedProductRepository.findByUserIdOrderByViewedAtDesc(userId).stream().limit(4).toList();

        model.addAttribute("user", currentUser);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("pendingOrders", pendingOrders);
        model.addAttribute("confirmedOrders", confirmedOrders);
        model.addAttribute("shippingOrders", shippingOrders);
        model.addAttribute("deliveredOrders", deliveredOrders);
        model.addAttribute("cancelledOrders", cancelledOrders);
        model.addAttribute("wishlistCount", wishlistCount);
        model.addAttribute("viewedCount", viewedCount);
        model.addAttribute("cartItemsCount", cartItemsCount);
        model.addAttribute("recentOrders", recentOrders);
        model.addAttribute("recentWishlist", recentWishlist);
        model.addAttribute("recentViewed", recentViewed);

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
        return "product";
    }

    // 📄 Chi tiết sản phẩm
    @GetMapping("product/{id}")
    public String productDetail(@PathVariable("id") Long id, Model model) {
        var opt = productRepository.findById(id);
        if (opt.isEmpty()) {
            return "redirect:/";
        }

        // Load reviews
        var reviews = reviewRepository.findByProductIdOrderByCreatedAtDesc(id);

        model.addAttribute("product", opt.get());
        model.addAttribute("reviews", reviews);
        return "product-detail";
    }

    @Autowired
    private com.oneshop.repository.AddressRepository addressRepository;

    // 👤 Trang hồ sơ người dùng (user/profile.html)
    @GetMapping("/profile")
    public String profile(Model model) {
        User u = userService.getCurrentUser();
        if (u == null) {
            return "redirect:/auth/login";
        }
        // Load danh sách địa chỉ
        var addresses = addressRepository.findByUserId(u.getId());

        model.addAttribute("user", u);
        model.addAttribute("addresses", addresses);
        return "user/profile";
    }

    // 📜 Lịch sử đơn hàng
    @GetMapping("/orders")
    public String orders(@RequestParam(value = "status", required = false) String status,
            Model model) {
        User current = userService.getCurrentUser();
        if (current == null) {
            return "redirect:/auth/login";
        }

        List<Order> ordersList;
        if (status != null && !status.isBlank()) {
            try {
                var st = com.oneshop.entity.OrderStatus.valueOf(status);
                ordersList = orderRepository.findByCustomer_User_IdAndStatusOrderByOrderDateDesc(current.getId(), st);
                model.addAttribute("currentStatus", status);
            } catch (IllegalArgumentException ex) {
                ordersList = orderRepository.findByCustomer_User_IdOrderByOrderDateDesc(current.getId());
            }
        } else {
            ordersList = orderRepository.findByCustomer_User_IdOrderByOrderDateDesc(current.getId());
        }
        model.addAttribute("orders", ordersList);

        return "user/order_history";
    }

    // 📦 Chi tiết đơn hàng
    @GetMapping("/order/{id}")
    public String orderDetail(@PathVariable Long id,
            @RequestParam(required = false) String cancelled,
            Model model) {
        User current = userService.getCurrentUser();
        if (current == null) {
            return "redirect:/auth/login";
        }

        // Tìm đơn hàng
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            return "redirect:/user/orders";
        }

        // Kiểm tra xem đơn hàng có thuộc về user hiện tại không
        if (!order.getCustomer().getUser().getId().equals(current.getId())) {
            return "redirect:/user/orders";
        }

        // Lấy danh sách sản phẩm trong đơn hàng
        var orderDetails = orderDetailRepository.findByOrderId(id);

        model.addAttribute("order", order);
        model.addAttribute("orderDetails", orderDetails);

        // Hiển thị thông báo hủy đơn thành công
        if ("true".equals(cancelled) && order.getStatus() == OrderStatus.CANCELLED) {
            model.addAttribute("successMessage", "Đơn hàng đã được hủy thành công!");
        }

        return "user/order_detail";
    }

    // ❌ Hủy đơn hàng
    @PostMapping("/order/{id}/cancel")
    public String cancelOrder(@PathVariable Long id, Model model) {
        User current = userService.getCurrentUser();
        if (current == null) {
            return "redirect:/auth/login";
        }

        // Tìm đơn hàng
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            return "redirect:/user/orders";
        }

        // Kiểm tra xem đơn hàng có thuộc về user hiện tại không
        if (!order.getCustomer().getUser().getId().equals(current.getId())) {
            return "redirect:/user/orders";
        }

        // Chỉ cho phép hủy đơn hàng khi đang ở trạng thái PENDING
        if (order.getStatus() == OrderStatus.PENDING) {
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);

            model.addAttribute("successMessage", "Đơn hàng đã được hủy thành công!");
        } else {
            model.addAttribute("errorMessage", "Không thể hủy đơn hàng ở trạng thái hiện tại!");
        }

        // Redirect về trang chi tiết với thông báo
        return "redirect:/user/order/" + id + "?cancelled=true";
    }

    // ✏️ Cập nhật thông tin người dùng
    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam("email") String email) {
        User current = userService.getCurrentUser();
        if (current == null) {
            return "redirect:/auth/login";
        }

        current.setEmail(email);
        userService.updateProfile(current);

        return "redirect:/user/profile";
    }
}
