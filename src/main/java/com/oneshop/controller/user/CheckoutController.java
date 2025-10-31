package com.oneshop.controller.user;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oneshop.dto.CartByShopDTO;
import com.oneshop.entity.CartItemEntity;
import com.oneshop.entity.Customer;
import com.oneshop.entity.Order;
import com.oneshop.entity.OrderDetail;
import com.oneshop.entity.OrderStatus;
import com.oneshop.entity.PaymentMethod;
import com.oneshop.entity.Promotion;
import com.oneshop.repository.AddressRepository;
import com.oneshop.repository.CartRepository;
import com.oneshop.repository.CustomerRepository;
import com.oneshop.repository.OrderDetailRepository;
import com.oneshop.repository.OrderRepository;
import com.oneshop.repository.PaymentMethodRepository;
import com.oneshop.repository.ProductRepository;
import com.oneshop.repository.PromotionRepository;
import com.oneshop.repository.ShopRepository;
import com.oneshop.service.user.UserService;
import com.oneshop.service.NotificationService;

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
    private CustomerRepository customerRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private ShopRepository shopRepository;

    @GetMapping
    public String view(Model model) {
        var current = userService.getCurrentUser();
        if (current == null) {
            return "redirect:/login";
        }
        
        var cart = cartRepo.findByUserId(current.getId()).orElse(null);
        
        // Group cart items by shop
        List<CartByShopDTO> cartsByShop = new ArrayList<>();
        BigDecimal grandTotal = BigDecimal.ZERO;
        
        if (cart != null && cart.getItems() != null && !cart.getItems().isEmpty()) {
            // Group items by shopId
            Map<Long, List<CartItemEntity>> itemsByShop = cart.getItems().stream()
                .filter(item -> item.getProduct() != null && item.getProduct().getShopId() != null)
                .collect(Collectors.groupingBy(item -> item.getProduct().getShopId()));
            
            LocalDateTime now = LocalDateTime.now();
            
            // Create CartByShopDTO for each shop
            for (Map.Entry<Long, List<CartItemEntity>> entry : itemsByShop.entrySet()) {
                Long shopId = entry.getKey();
                List<CartItemEntity> shopItems = entry.getValue();
                
                // Calculate subtotal for this shop
                BigDecimal shopSubtotal = BigDecimal.ZERO;
                for (CartItemEntity item : shopItems) {
                    if (item.getProduct() != null) {
                        BigDecimal itemTotal = item.getProduct().getPrice()
                            .multiply(BigDecimal.valueOf(item.getQuantity()));
                        shopSubtotal = shopSubtotal.add(itemTotal);
                    }
                }
                
                // Get shop name
                String shopName = shopRepository.findById(shopId)
                    .map(shop -> shop.getName())
                    .orElse("Shop #" + shopId);
                
                // Get active promotions for this shop
                List<Promotion> shopPromotions = promotionRepository
                    .findByShopIdAndActiveTrueAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                        shopId, now, now
                    );
                
                CartByShopDTO shopCart = CartByShopDTO.builder()
                    .shopId(shopId)
                    .shopName(shopName != null ? shopName : "Shop #" + shopId)
                    .items(shopItems)
                    .subtotal(shopSubtotal)
                    .promotions(shopPromotions)
                    .build();
                
                cartsByShop.add(shopCart);
                grandTotal = grandTotal.add(shopSubtotal);
            }
        }
        
        model.addAttribute("cart", cart);
        model.addAttribute("cartsByShop", cartsByShop);
        model.addAttribute("grandTotal", grandTotal);
        model.addAttribute("paymentMethods", paymentMethodRepo.findByActiveTrue());
        model.addAttribute("addresses", addressRepository.findByUserId(current.getId()));
        
        return "user/checkout";
    }

    @PostMapping
    public String placeOrder(
            @RequestParam(required = false) Long addressId,
            @RequestParam(required = false) String shippingAddress,
            @RequestParam String payment,
            @RequestParam(required = false) Long promotionId) {
        var current = userService.getCurrentUser();
        if (current == null) {
            return "redirect:/auth/login";
        }
        var cart = cartRepo.findByUserId(current.getId()).orElse(null);
        if (cart == null || cart.getItems().isEmpty()) {
            return "redirect:/user/cart";
        }

        // Calculate subtotal
        BigDecimal total = BigDecimal.ZERO;
        for (CartItemEntity it : cart.getItems()) {
            var p = productRepo.findById(it.getProduct().getId()).orElseThrow();
            total = total.add(p.getPrice().multiply(BigDecimal.valueOf(it.getQuantity())));
        }
        
        // Apply promotion discount if selected
        if (promotionId != null) {
            var promoOpt = promotionRepository.findById(promotionId);
            if (promoOpt.isPresent()) {
                Promotion promo = promoOpt.get();
                LocalDateTime now = LocalDateTime.now();
                
                // Verify promotion is still active and valid
                if (promo.isActive() 
                    && !now.isBefore(promo.getStartTime()) 
                    && !now.isAfter(promo.getEndTime())) {
                    
                    BigDecimal discount = BigDecimal.ZERO;
                    switch (promo.getDiscountType()) {
                        case PERCENTAGE:
                            discount = total.multiply(promo.getDiscountValue())
                                .divide(BigDecimal.valueOf(100));
                            break;
                        case AMOUNT:
                            discount = promo.getDiscountValue();
                            break;
                    }
                    
                    total = total.subtract(discount);
                    if (total.compareTo(BigDecimal.ZERO) < 0) {
                        total = BigDecimal.ZERO;
                    }
                }
            }
        }

        PaymentMethod pm = paymentMethodRepo.findByName(payment).orElseGet(()
                -> paymentMethodRepo.save(PaymentMethod.builder().name(payment).displayName(payment).active(true).build())
        );

        // Find or create Customer from current User
        Customer customer = customerRepo.findByUserId(current.getId()).orElseGet(() -> {
            Customer c = Customer.builder()
                    .user(current)
                    .fullName(current.getUsername())
                    .build();
            return customerRepo.save(c);
        });

        // Determine shipping address from Address table (prioritize addressId, then default), fallback to shippingAddress parameter
        String shippingAddressStr = null;
        if (addressId != null) {
            shippingAddressStr = addressRepository.findById(addressId)
                    .filter(a -> a.getUserId().equals(current.getId()))
                    .map(a -> a.getAddress())
                    .orElse(null);
        }
        if (shippingAddressStr == null) {
            shippingAddressStr = addressRepository.findFirstByUserIdAndIsDefaultTrue(current.getId())
                    .map(a -> a.getAddress())
                    .orElse(shippingAddress);
        }
        if (shippingAddressStr == null || shippingAddressStr.isBlank()) {
            return "redirect:/user/checkout"; // require address selection/input
        }

        // Get shopId from cart items (assuming single shop for now)
        Long shopId = cart.getItems().isEmpty() ? 0L : cart.getItems().get(0).getProduct().getShopId();

        Order order = Order.builder()
                .shopId(shopId)
                .customer(customer)
                .status(OrderStatus.PENDING)
                .totalAmount(total)
                .paymentMethod(pm)
                .shippingAddress(shippingAddressStr)
                .build();
        order = orderRepo.save(order);

        // Send notification for new order to vendor
        if (order.getShopId() != null && order.getShopId() > 0) {
            notificationService.createNewOrderNotification(order.getShopId(), order.getId(), "#" + order.getId());
        }

        for (CartItemEntity it : cart.getItems()) {
            var p = productRepo.findById(it.getProduct().getId()).orElseThrow();
            orderDetailRepo.save(OrderDetail.builder()
                    .order(order)
                    .product(p)
                    .quantity(it.getQuantity())
                    .price(p.getPrice())
                    .build());
            
            // Update product stock and sold count
            p.setStock(p.getStock() - it.getQuantity());
            p.setSold(p.getSold() + it.getQuantity());
            productRepo.save(p);
        }

        cart.getItems().clear();
        cartRepo.save(cart);

        if ("COD".equalsIgnoreCase(payment)) {
            return "redirect:/user/orders";
        } else {
            if ("VNPAY".equalsIgnoreCase(payment)) {
                return "redirect:/payment/vnpay/create?orderId=" + order.getId();
            }
            // TODO: MoMo flow
            return "redirect:/user/orders";
        }
    }
}
