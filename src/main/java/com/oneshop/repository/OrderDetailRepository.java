package com.oneshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.oneshop.entity.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    List<OrderDetail> findByOrderId(Long orderId);

    @Query("SELECT CASE WHEN COUNT(od) > 0 THEN true ELSE false END FROM OrderDetail od "
            + "WHERE od.order.customer.user.id = :userId AND od.product.id = :productId")
    boolean existsPurchasedByUserAndProduct(Long userId, Long productId);
}
