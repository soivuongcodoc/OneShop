package com.oneshop.repository;

import com.oneshop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByShopId(Long shopId);
    List<Order> findByShopIdOrderByOrderDateDesc(Long shopId);
    List<Order> findByShopIdAndStatus(Long shopId, String status);
}
