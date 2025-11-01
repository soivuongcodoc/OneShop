package com.oneshop.service.admin;

import java.util.List;
import java.util.Optional;

import com.oneshop.dto.admin.UserForm;
import com.oneshop.entity.User;

public interface UserService {
    List<User> searchUsers(String keyword);
    void deleteUser(Long id);
    void deactivateUser(Long id);

    // Mới: CRUD cơ bản cho admin UI
    Optional<User> findById(Long id);
    void createUser(UserForm form);
    void updateUser(Long id, UserForm form);

    // Trả về danh sách role có thể gán (ví dụ: ROLE_USER, ROLE_VENDOR, ROLE_ADMIN)
    List<String> getAllRoles();
}
