package com.oneshop.service.user;

import com.oneshop.entity.User;

public interface UserService {

    User getCurrentUser();

    void updateProfile(User user);
}
