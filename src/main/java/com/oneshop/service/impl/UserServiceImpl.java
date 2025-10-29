package com.oneshop.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.oneshop.entity.User;
import com.oneshop.repository.UserRepository;
import com.oneshop.service.user.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal() == null) {
            return null;
        }
        Object principal = auth.getPrincipal();
        String usernameOrEmail;
        if (principal instanceof org.springframework.security.core.userdetails.User) {
            usernameOrEmail = ((org.springframework.security.core.userdetails.User) principal).getUsername();
        } else {
            usernameOrEmail = principal.toString();
        }

        // Tìm theo username trước, nếu không có thì tìm theo email
        Optional<User> u = userRepository.findByUsername(usernameOrEmail);
        if (u.isEmpty()) {
            u = userRepository.findByEmail(usernameOrEmail);
        }
        return u.orElse(null);
    }

    @Override
    public void updateProfile(User user) {
        userRepository.save(user);
    }
}
