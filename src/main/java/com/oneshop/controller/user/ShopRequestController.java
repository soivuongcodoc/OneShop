package com.oneshop.controller.user;

import com.oneshop.entity.Role;
import com.oneshop.entity.ShopRequest;
import com.oneshop.entity.User;
import com.oneshop.repository.RoleRepository;
import com.oneshop.repository.ShopRepository;
import com.oneshop.repository.ShopRequestRepository;
import com.oneshop.repository.UserRepository;
import com.oneshop.service.user.UserService;
import com.oneshop.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/user/shop-request")
@RequiredArgsConstructor
public class ShopRequestController {
    
    private final ShopRequestRepository shopRequestRepository;
    private final ShopRepository shopRepository;
    private final UserService userService;
    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @GetMapping
    public String showRequestForm(Model model) {
        User user = userService.getCurrentUser();
        
        // Check if user already has a shop
        if (shopRepository.findById(user.getId()).isPresent()) {
            return "redirect:/vendor/dashboard";
        }
        
        // Check if user has pending request
        var pendingRequest = shopRequestRepository.findByUserAndStatus(user, ShopRequest.RequestStatus.PENDING);
        if (pendingRequest.isPresent()) {
            model.addAttribute("pendingRequest", pendingRequest.get());
            return "user/shop-request-status";
        }
        
        // Get all user's requests
        var requests = shopRequestRepository.findByUserOrderByRequestDateDesc(user);
        model.addAttribute("requests", requests);
        model.addAttribute("shopRequest", new ShopRequest());
        
        return "user/shop-request-form";
    }

    @PostMapping
    public String submitRequest(@ModelAttribute ShopRequest shopRequest,
                                RedirectAttributes redirectAttributes) {
        User user = userService.getCurrentUser();
        
        // Check if user already has a shop
        if (shopRepository.findById(user.getId()).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Bạn đã có shop rồi!");
            return "redirect:/vendor/dashboard";
        }
        
        // Check if user has pending request
        var pendingRequest = shopRequestRepository.findByUserAndStatus(user, ShopRequest.RequestStatus.PENDING);
        if (pendingRequest.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Bạn đã có yêu cầu đang chờ duyệt!");
            return "redirect:/user/shop-request";
        }
        
        shopRequest.setUser(user);
        shopRequest.setStatus(ShopRequest.RequestStatus.PENDING);
        shopRequest = shopRequestRepository.save(shopRequest);
        
        // Gửi thông báo cho tất cả admin
        Role adminRole = roleRepository.findByName("ADMIN").orElse(null);
        if (adminRole != null) {
            List<User> admins = userRepository.findByRolesContaining(adminRole);
            for (User admin : admins) {
                notificationService.createNewShopRequestNotification(
                    admin.getId(), 
                    shopRequest.getId(), 
                    user.getUsername(), 
                    shopRequest.getShopName()
                );
            }
        }
        
        redirectAttributes.addFlashAttribute("success", "Gửi yêu cầu thành công! Vui lòng đợi admin duyệt.");
        return "redirect:/user/shop-request";
    }
}
