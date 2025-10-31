package com.oneshop.controller;

import com.oneshop.entity.User;
import com.oneshop.service.NotificationService;
import com.oneshop.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {
    
    private final NotificationService notificationService;
    private final UserService userService;

    @ModelAttribute("unreadNotificationCount")
    public Long getUnreadNotificationCount() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
                User currentUser = userService.getCurrentUser();
                if (currentUser != null) {
                    return notificationService.getUnreadCount(currentUser.getId());
                }
            }
        } catch (Exception e) {
            // User not logged in or error getting user
        }
        return 0L;
    }
}
