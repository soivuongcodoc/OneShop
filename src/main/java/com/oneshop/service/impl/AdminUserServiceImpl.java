package com.oneshop.service.impl;

import com.oneshop.entity.Role;
import com.oneshop.entity.User;
import com.oneshop.entity.Order;
import com.oneshop.repository.*;
import com.oneshop.service.admin.UserService;
import com.oneshop.dto.admin.UserForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // For cleanup before hard delete
    private final CartRepository cartRepository;
    private final WishlistRepository wishlistRepository;
    private final ViewedProductRepository viewedProductRepository;
    private final NotificationRepository notificationRepository;
    private final AddressRepository addressRepository;
    private final ReviewRepository reviewRepository;
    private final ShopRepository shopRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;
    private final ShopRequestRepository shopRequestRepository;

    @Override
    public List<User> searchUsers(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return userRepository.findAll();
        }
        return userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (id == null) return;

        // 1) Remove dependent records that may have FK to users
        // carts (has FK to users), will cascade to cart_items
        cartRepository.deleteByUserId(id);

        // wishlists, viewed products, notifications, addresses, reviews
        wishlistRepository.deleteByUserId(id);
        viewedProductRepository.deleteByUserId(id);
        notificationRepository.deleteByUserId(id);
        addressRepository.deleteByUserId(id);
        reviewRepository.deleteByUserId(id);

        // 2) Shop and all related data (products, orders, promotions)
        if (shopRepository.existsById(id)) {
            // Delete orders & order details for this shop
            List<Order> shopOrders = orderRepository.findByShopId(id);
            for (Order order : shopOrders) {
                orderDetailRepository.deleteAll(orderDetailRepository.findByOrderId(order.getId()));
            }
            orderRepository.deleteByShopId(id);
            
            // Delete products and promotions
            productRepository.deleteByShopId(id);
            promotionRepository.deleteByShopId(id);
            
            // Finally delete shop
            shopRepository.deleteById(id);
        }

        // 3) ShopRequests (user_id FK to users)
        userRepository.findById(id).ifPresent(user -> {
            shopRequestRepository.deleteByUser(user);
        });

        // 4) Customer: detach link to user to avoid FK block, but keep customer (and orders)
        customerRepository.findByUserId(id).ifPresent(c -> {
            c.setUser(null);
            customerRepository.save(c);
        });

        // 5) Finally delete the user
        userRepository.deleteById(id);
    }

    @Override
    public void deactivateUser(Long id) {
        userRepository.findById(id).ifPresent(u -> {
            u.setEnabled(false);
            userRepository.save(u);
        });
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

        // assign roles (single role from form.role, or default ROLE_USER)
        final String requestedRole = determineRole(form);
        
        Role r = roleRepository.findByName(requestedRole)
                .orElseThrow(() -> new RuntimeException("Role not found: " + requestedRole));
        Set<Role> roles = new HashSet<>();
        roles.add(r);
        
        u.setRoles(roles);
        userRepository.save(u);
    }
    
    private String determineRole(UserForm form) {
        String role = form.getRole();
        
        // Fallback to roles list if role is not provided (backward compatibility)
        if ((role == null || role.isBlank()) && form.getRoles() != null && !form.getRoles().isEmpty()) {
            role = form.getRoles().get(0);
        }
        
        // Default to ROLE_USER if nothing provided
        if (role == null || role.isBlank()) {
            role = "ROLE_USER";
        }
        
        return role;
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

        // update role (single role from form.role)
        final String requestedRole = determineRole(form);
        Role r = roleRepository.findByName(requestedRole)
                .orElseThrow(() -> new RuntimeException("Role not found: " + requestedRole));
        Set<Role> roles = new HashSet<>();
        roles.add(r);
        u.setRoles(roles);

        userRepository.save(u);
    }

    @Override
    public List<String> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }
}
