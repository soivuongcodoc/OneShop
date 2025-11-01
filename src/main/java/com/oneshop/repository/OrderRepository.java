package com.oneshop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.oneshop.entity.Order;
import com.oneshop.entity.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByShopId(Long shopId);

    List<Order> findByShopIdOrderByOrderDateDesc(Long shopId);

    List<Order> findByShopIdAndStatus(Long shopId, OrderStatus status);

    List<Order> findByShopIdAndStatusOrderByOrderDateDesc(Long shopId, OrderStatus status);

    Optional<Order> findByIdAndShopId(Long id, Long shopId);

    List<Order> findByCustomer_User_IdOrderByOrderDateDesc(Long userId);

    List<Order> findByCustomer_User_IdAndStatusOrderByOrderDateDesc(Long userId, OrderStatus status);

    @Transactional
    void deleteByShopId(Long shopId);
}
