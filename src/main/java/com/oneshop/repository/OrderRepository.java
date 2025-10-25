package com.oneshop.repository;

import com.oneshop.entity.Order;
import com.oneshop.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByShopId(Long shopId);
    List<Order> findByShopIdOrderByOrderDateDesc(Long shopId);
    List<Order> findByShopIdAndStatus(Long shopId, OrderStatus status);
    List<Order> findByShopIdAndStatusOrderByOrderDateDesc(Long shopId, OrderStatus status);
    Optional<Order> findByIdAndShopId(Long id, Long shopId);
}
