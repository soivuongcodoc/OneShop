package com.oneshop.controller.user;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oneshop.entity.CartItemEntity;
import com.oneshop.entity.Coupon;
import com.oneshop.entity.Customer;
import com.oneshop.entity.Order;
import com.oneshop.entity.OrderDetail;
import com.oneshop.entity.OrderStatus;
import com.oneshop.entity.PaymentMethod;
import com.oneshop.repository.CartRepository;
import com.oneshop.repository.CouponRepository;
import com.oneshop.repository.CustomerRepository;
import com.oneshop.repository.OrderDetailRepository;
import com.oneshop.repository.OrderRepository;
import com.oneshop.repository.PaymentMethodRepository;
import com.oneshop.repository.ProductRepository;
import com.oneshop.service.user.UserService;

@Controller
@RequestMapping("/user/checkout")
public class CheckoutController {

    @Autowired
    private CartRepository cartRepo;
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private OrderDetailRepository orderDetailRepo;
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private PaymentMethodRepository paymentMethodRepo;
    @Autowired
    private CouponRepository couponRepo;
    @Autowired
    private CustomerRepository customerRepo;
    @Autowired
    private UserService userService;

    @GetMapping
    public String view(Model model) {
        var current = userService.getCurrentUser();
        if (current == null) {
            return "redirect:/auth/login";
        }
        var cart = cartRepo.findByUserId(current.getId()).orElse(null);
        model.addAttribute("cart", cart);
        model.addAttribute("paymentMethods", paymentMethodRepo.findByActiveTrue());
        return "user/checkout";
    }

    @PostMapping
    public String placeOrder(@RequestParam String shippingAddress,
            @RequestParam String payment,
            @RequestParam(required = false) String coupon) {
        var current = userService.getCurrentUser();
        if (current == null) {
            return "redirect:/auth/login";
        }
        var cart = cartRepo.findByUserId(current.getId()).orElse(null);
        if (cart == null || cart.getItems().isEmpty()) {
            return "redirect:/user/cart";
        }

        BigDecimal total = BigDecimal.ZERO;
        for (CartItemEntity it : cart.getItems()) {
            var p = productRepo.findById(it.getProduct().getId()).orElseThrow();
            total = total.add(p.getPrice().multiply(BigDecimal.valueOf(it.getQuantity())));
        }
        if (coupon != null && !coupon.isBlank()) {
            var opt = couponRepo.findByCodeAndActiveTrue(coupon.trim());
            if (opt.isPresent()) {
                var c = opt.get();
                if (c.getDiscountType() == Coupon.DiscountType.PERCENTAGE) {
                    total = total.subtract(total.multiply(c.getDiscountValue()).divide(BigDecimal.valueOf(100)));
                } else {
                    total = total.subtract(c.getDiscountValue());
                }
                if (total.compareTo(BigDecimal.ZERO) < 0) {
                    total = BigDecimal.ZERO;
                }
            }
        }

        PaymentMethod pm = paymentMethodRepo.findByName(payment).orElseGet(()
                -> paymentMethodRepo.save(PaymentMethod.builder().name(payment).displayName(payment).active(true).build())
        );

        // Tìm hoặc tạo Customer từ User hiện tại
        Customer customer = customerRepo.findByUserId(current.getId()).orElseGet(() -> {
            Customer c = Customer.builder()
                    .user(current)
                    .fullName(current.getUsername()) // Dùng tạm username, sau user có thể cập nhật
                    .address(shippingAddress)
                    .build();
            return customerRepo.save(c);
        });

        Order order = Order.builder()
                .shopId(0L) // nếu multi-shop, cần tách theo shop
                .customer(customer)
                .status(OrderStatus.PENDING)
                .totalAmount(total)
                .paymentMethod(pm)
                .shippingAddress(shippingAddress)
                .build();
        order = orderRepo.save(order);

        for (CartItemEntity it : cart.getItems()) {
            var p = productRepo.findById(it.getProduct().getId()).orElseThrow();
            orderDetailRepo.save(OrderDetail.builder()
                    .order(order)
                    .product(p)
                    .quantity(it.getQuantity())
                    .price(p.getPrice())
                    .build());
        }

        cart.getItems().clear();
        cartRepo.save(cart);

        if ("COD".equalsIgnoreCase(payment)) {
            return "redirect:/user/orders";
        } else {
            if ("VNPAY".equalsIgnoreCase(payment)) {
                return "redirect:/payment/vnpay/create?orderId=" + order.getId();
            }
            // TODO: MoMo flow tương tự
            return "redirect:/user/orders";
        }
    }
}
