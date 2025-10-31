package com.oneshop.controller.user;

import com.oneshop.entity.Notification;
import com.oneshop.entity.User;
import com.oneshop.service.NotificationService;
import com.oneshop.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationPageController {
    
    private final NotificationService notificationService;
    private final UserService userService;

    @GetMapping
    public String listNotifications(Model model) {
        User currentUser = userService.getCurrentUser();
        List<Notification> notifications = notificationService.getRecentNotifications(currentUser.getId());
        Long unreadCount = notificationService.getUnreadCount(currentUser.getId());
        
        model.addAttribute("notifications", notifications);
        model.addAttribute("unreadCount", unreadCount);
        
        return "notifications";
    }

    @PostMapping("/{id}/read")
    @ResponseBody
    public String markAsRead(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        notificationService.markAsRead(id, currentUser.getId());
        return "OK";
    }

    @PostMapping("/read-all")
    @ResponseBody
    public String markAllAsRead() {
        User currentUser = userService.getCurrentUser();
        notificationService.markAllAsRead(currentUser.getId());
        return "OK";
    }
}
