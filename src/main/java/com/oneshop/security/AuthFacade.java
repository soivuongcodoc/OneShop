package com.oneshop.security;

import com.oneshop.entity.User;
import com.oneshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthFacade {
    private final UserRepository userRepo;

    public Long currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return null;
        
        Object principal = auth.getPrincipal();
        // Skip anonymous users
        if ("anonymousUser".equals(principal)) return null;
        
        String username = (principal instanceof String) ? (String) principal : String.valueOf(principal);
        return userRepo.findByUsername(username)
                .map(User::getId)
                .orElseGet(() -> userRepo.findByEmail(username)
                        .map(User::getId)
                        .orElse(null));
    }

    public Long requireUserId() {
        Long id = currentUserId();
        if (id == null) throw new RuntimeException("Không xác định được người dùng hiện tại");
        return id;
    }
}
