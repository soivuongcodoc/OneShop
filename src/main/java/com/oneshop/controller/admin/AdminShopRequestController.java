package com.oneshop.controller.admin;

import com.oneshop.entity.Role;
import com.oneshop.entity.Shop;
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

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin/shop-requests")
@RequiredArgsConstructor
public class AdminShopRequestController {
    
    private final ShopRequestRepository shopRequestRepository;
    private final ShopRepository shopRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserService userService;
    private final NotificationService notificationService;

    @GetMapping
    public String listRequests(Model model) {
        List<ShopRequest> pendingRequests = shopRequestRepository.findByStatus(ShopRequest.RequestStatus.PENDING);
        List<ShopRequest> allRequests = shopRequestRepository.findAll();
        
        model.addAttribute("pendingRequests", pendingRequests);
        model.addAttribute("allRequests", allRequests);
        
        return "admin/shop-requests";
    }

    @PostMapping("/{id}/approve")
    public String approveRequest(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        ShopRequest request = shopRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        
        if (request.getStatus() != ShopRequest.RequestStatus.PENDING) {
            redirectAttributes.addFlashAttribute("error", "Yêu cầu đã được xử lý rồi!");
            return "redirect:/admin/shop-requests";
        }
        
        User user = request.getUser();
        
        // Change role from USER (1) to VENDOR (2)
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("ROLE_USER role not found"));
        Role vendorRole = roleRepository.findByName("ROLE_VENDOR")
                .orElseThrow(() -> new RuntimeException("ROLE_VENDOR role not found"));
        
        user.getRoles().clear(); // Remove all roles
        user.getRoles().add(vendorRole); // Add VENDOR role only
        user = userRepository.save(user); // Save and get updated user
        
        // Create shop for user (don't set ID, let @MapsId handle it)
        Shop shop = new Shop();
        shop.setVendor(user);
        shop.setName(request.getShopName());
        shop.setDescription(request.getDescription());
        shop.setAddress(request.getAddress());
        shop.setPhone(request.getPhone());
        shop.setCreatedAt(LocalDateTime.now());
        shopRepository.save(shop);
        
        // Update request status
        request.setStatus(ShopRequest.RequestStatus.APPROVED);
        request.setProcessedDate(LocalDateTime.now());
        request.setProcessedBy(userService.getCurrentUser());
        shopRequestRepository.save(request);
        
        // Gửi thông báo cho user
        notificationService.createShopRequestApprovedNotification(
            user.getId(), 
            request.getId(), 
            request.getShopName()
        );
        
        redirectAttributes.addFlashAttribute("success", 
            "Đã duyệt yêu cầu! User " + user.getUsername() + " đã trở thành vendor.");
        
        return "redirect:/admin/shop-requests";
    }

    @PostMapping("/{id}/reject")
    public String rejectRequest(@PathVariable Long id, 
                                @RequestParam String rejectReason,
                                RedirectAttributes redirectAttributes) {
        ShopRequest request = shopRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        
        if (request.getStatus() != ShopRequest.RequestStatus.PENDING) {
            redirectAttributes.addFlashAttribute("error", "Yêu cầu đã được xử lý rồi!");
            return "redirect:/admin/shop-requests";
        }
        
        request.setStatus(ShopRequest.RequestStatus.REJECTED);
        request.setRejectReason(rejectReason);
        request.setProcessedDate(LocalDateTime.now());
        request.setProcessedBy(userService.getCurrentUser());
        shopRequestRepository.save(request);
        
        // Gửi thông báo cho user
        notificationService.createShopRequestRejectedNotification(
            request.getUser().getId(), 
            request.getId(), 
            request.getShopName(), 
            rejectReason
        );
        
        redirectAttributes.addFlashAttribute("success", "Đã từ chối yêu cầu!");
        
        return "redirect:/admin/shop-requests";
    }
}
