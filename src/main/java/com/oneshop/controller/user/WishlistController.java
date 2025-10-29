package com.oneshop.controller.user;

import com.oneshop.entity.Wishlist;
import com.oneshop.repository.ProductRepository;
import com.oneshop.repository.WishlistRepository;
import com.oneshop.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user/wishlist")
public class WishlistController {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    @GetMapping
    public String list(Model model) {
        var current = userService.getCurrentUser();
        if (current == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("items", wishlistRepository.findByUserId(current.getId()));
        return "user/wishlist";
    }

    @PostMapping("/add/{productId}")
    public String add(@PathVariable Long productId) {
        var current = userService.getCurrentUser();
        if (current == null) {
            return "redirect:/auth/login";
        }
        var p = productRepository.findById(productId);
        if (p.isEmpty()) {
            return "redirect:/";
        }
        wishlistRepository.findByUserIdAndProductId(current.getId(), productId).orElseGet(()
                -> wishlistRepository.save(Wishlist.builder().userId(current.getId()).product(p.get()).build())
        );
        return "redirect:/user/wishlist";
    }

    @PostMapping("/remove/{productId}")
    public String remove(@PathVariable Long productId) {
        var current = userService.getCurrentUser();
        if (current == null) {
            return "redirect:/auth/login";
        }
        wishlistRepository.deleteByUserIdAndProductId(current.getId(), productId);
        return "redirect:/user/wishlist";
    }
}
