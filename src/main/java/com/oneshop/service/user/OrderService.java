package com.oneshop.service.user;

import java.util.List;

import com.oneshop.entity.Order;

public interface OrderService {

    List<Order> findByUser(Long userId);
}
