package com.oneshop.controller.user;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oneshop.dto.CartByShopDTO;
import com.oneshop.dto.CartItem;
import com.oneshop.entity.Cart;
import com.oneshop.entity.CartItemEntity;
import com.oneshop.entity.Product;
import com.oneshop.repository.CartRepository;
import com.oneshop.repository.ProductRepository;
import com.oneshop.repository.ShopRepository;
import com.oneshop.service.user.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/cart")
public class CartController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserService userService;
    
    @Autowired
    private ShopRepository shopRepository;

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        var current = userService.getCurrentUser();
        List<CartByShopDTO> cartsByShop = new ArrayList<>();
        double grandTotal = 0;

        if (current != null) {
            Cart cart = cartRepository.findByUserId(current.getId()).orElse(null);
            
            if (cart != null && cart.getItems() != null && !cart.getItems().isEmpty()) {
                // Group items by shopId
                Map<Long, List<CartItemEntity>> itemsByShop = cart.getItems().stream()
                    .filter(item -> item.getProduct() != null && item.getProduct().getShopId() != null)
                    .collect(Collectors.groupingBy(item -> item.getProduct().getShopId()));
                
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
                    
                    CartByShopDTO shopCart = CartByShopDTO.builder()
                        .shopId(shopId)
                        .shopName(shopName)
                        .items(shopItems)
                        .subtotal(shopSubtotal)
                        .build();
                    
                    cartsByShop.add(shopCart);
                    grandTotal += shopSubtotal.doubleValue();
                }
            }
        } else {
            // Session cart (not logged in) - simple list without shop grouping
            List<CartItem> items = (List<CartItem>) session.getAttribute("CART");
            if (items != null) {
                for (CartItem item : items) {
                    grandTotal += item.getPrice() * item.getQuantity();
                }
            }
            model.addAttribute("cart", items != null ? items : new ArrayList<>());
        }

        model.addAttribute("cartsByShop", cartsByShop);
        model.addAttribute("grandTotal", grandTotal);
        model.addAttribute("isLoggedIn", current != null);
        return "user/cart";
    }

    @PostMapping("/add/{id}")
    public String addToCart(@PathVariable("id") Long id,
            @RequestParam(value = "qty", defaultValue = "1") int qty,
            HttpSession session) {
        Optional<Product> p = productRepository.findById(id);
        if (p.isEmpty()) {
            return "redirect:/";
        }
        var current = userService.getCurrentUser();
        if (current != null) {
            Cart cart = cartRepository.findByUserId(current.getId()).orElseGet(() -> {
                Cart c = new Cart();
                c.setUserId(current.getId());
                return cartRepository.save(c);
            });
            boolean updated = false;
            for (CartItemEntity it : cart.getItems()) {
                if (it.getProduct().getId().equals(id)) {
                    it.setQuantity(it.getQuantity() + qty);
                    updated = true;
                    break;
                }
            }
            if (!updated) {
                CartItemEntity it = new CartItemEntity();
                it.setCart(cart);
                it.setProduct(p.get());
                it.setQuantity(qty);
                cart.getItems().add(it);
            }
            cartRepository.save(cart);
        } else {
            List<CartItem> cart = (List<CartItem>) session.getAttribute("CART");
            if (cart == null) {
                cart = new ArrayList<>();
            }
            boolean found = false;
            for (CartItem it : cart) {
                if (it.getProductId().equals(id)) {
                    it.setQuantity(it.getQuantity() + qty);
                    found = true;
                    break;
                }
            }
            if (!found) {
                CartItem it = new CartItem(id, p.get().getName(), qty, p.get().getPrice().doubleValue());
                cart.add(it);
            }
            session.setAttribute("CART", cart);
        }
        return "redirect:/user/cart";
    }

    @PostMapping("/remove/{id}")
    public String remove(@PathVariable("id") Long id, HttpSession session) {
        var current = userService.getCurrentUser();
        if (current != null) {
            Cart cart = cartRepository.findByUserId(current.getId()).orElse(null);
            if (cart != null) {
                cart.getItems().removeIf(i -> i.getProduct().getId().equals(id));
                cartRepository.save(cart);
            }
        } else {
            List<CartItem> cart = (List<CartItem>) session.getAttribute("CART");
            if (cart != null) {
                cart.removeIf(i -> i.getProductId().equals(id));
                session.setAttribute("CART", cart);
            }
        }
        return "redirect:/user/cart";
    }
}
