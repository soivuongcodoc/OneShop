package com.oneshop.service.impl;

import com.oneshop.entity.Role;
import com.oneshop.entity.User;
import com.oneshop.repository.RoleRepository;
import com.oneshop.repository.UserRepository;
import com.oneshop.service.admin.UserService;
import com.oneshop.dto.admin.UserForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> searchUsers(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return userRepository.findAll();
        }
        return userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void createUser(UserForm form) {
        if (form == null) throw new IllegalArgumentException("form is null");
        if (userRepository.existsByUsername(form.getUsername()))
            throw new RuntimeException("Username already exists");
        if (userRepository.existsByEmail(form.getEmail()))
            throw new RuntimeException("Email already exists");

        User u = new User();
        u.setUsername(form.getUsername());
        u.setEmail(form.getEmail());

        u.setEnabled(true); // Admin-created users should be enabled by default
        String rawPw = form.getPassword();
        if (rawPw == null || rawPw.isBlank()) {
            throw new RuntimeException("Password is required when creating a user");
        }
        u.setPassword(passwordEncoder.encode(rawPw));

        // assign roles (default ROLE_USER if none provided)
        Set<Role> roles = new HashSet<>();
        List<String> requested = form.getRoles();
        if (requested == null || requested.isEmpty()) {
            Role r = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));
            roles.add(r);
        } else {
            for (String rn : requested) {
                Role r = roleRepository.findByName(rn)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + rn));
                roles.add(r);
            }
        }
        u.setRoles(roles);
        userRepository.save(u);
    }

    @Override
    public void updateUser(Long id, UserForm form) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));

        if (form.getUsername() != null && !form.getUsername().isBlank() && !form.getUsername().equals(u.getUsername())) {
            if (userRepository.existsByUsername(form.getUsername())) {
                throw new RuntimeException("Username already exists");
            }
            u.setUsername(form.getUsername());
        }

        if (form.getEmail() != null && !form.getEmail().isBlank() && !form.getEmail().equals(u.getEmail())) {
            if (userRepository.existsByEmail(form.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
            u.setEmail(form.getEmail());
        }

        if (form.getPassword() != null && !form.getPassword().isBlank()) {
            u.setPassword(passwordEncoder.encode(form.getPassword()));
        }

        // update roles if provided
        List<String> requested = form.getRoles();
        if (requested != null) {
            Set<Role> roles = requested.stream()
                    .map(rn -> roleRepository.findByName(rn)
                            .orElseThrow(() -> new RuntimeException("Role not found: " + rn)))
                    .collect(Collectors.toSet());
            u.setRoles(roles);
        }

        userRepository.save(u);
    }

    @Override
    public List<String> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }
}
