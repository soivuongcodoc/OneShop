package com.oneshop.service.admin;

import com.oneshop.entity.User;
import java.util.List;

public interface UserService {
    List<User> searchUsers(String keyword);
    void deleteUser(Long id);
}
