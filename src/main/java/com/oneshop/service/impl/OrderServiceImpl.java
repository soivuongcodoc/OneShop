package com.oneshop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oneshop.entity.Order;
import com.oneshop.repository.OrderRepository;
import com.oneshop.service.user.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<Order> findByUser(Long userId) {
        return orderRepository.findByCustomer_User_IdOrderByOrderDateDesc(userId);
    }
}
