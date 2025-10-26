package com.oneshop.service.impl;

import com.oneshop.entity.User;
import com.oneshop.repository.UserRepository;
import com.oneshop.service.admin.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements UserService {

    private final UserRepository userRepository;

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
}
