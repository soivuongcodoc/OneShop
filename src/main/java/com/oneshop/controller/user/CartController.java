package com.oneshop.controller.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oneshop.dto.CartItem;
import com.oneshop.entity.Cart;
import com.oneshop.entity.CartItemEntity;
import com.oneshop.entity.Product;
import com.oneshop.repository.CartRepository;
import com.oneshop.repository.ProductRepository;
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

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        var current = userService.getCurrentUser();
        List<CartItem> items;
        double totalAmount = 0;

        if (current != null) {
            Cart cart = cartRepository.findByUserId(current.getId()).orElse(null);
            items = new ArrayList<>();
            if (cart != null && cart.getItems() != null) {
                for (CartItemEntity it : cart.getItems()) {
                    CartItem item = new CartItem(it.getProduct().getId(), it.getProduct().getName(), it.getQuantity(), it.getProduct().getPrice().doubleValue());
                    items.add(item);
                    totalAmount += item.getPrice() * item.getQuantity();
                }
            }
        } else {
            items = (List<CartItem>) session.getAttribute("CART");
            if (items == null) {
                items = new ArrayList<>();
            }
            for (CartItem item : items) {
                totalAmount += item.getPrice() * item.getQuantity();
            }
        }

        model.addAttribute("cart", items);
        model.addAttribute("totalAmount", totalAmount);
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
