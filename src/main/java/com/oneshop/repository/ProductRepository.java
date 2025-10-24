package com.oneshop.repository;

import com.oneshop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByShopId(Long shopId);
    List<Product> findByShopIdAndActiveTrue(Long shopId);
    java.util.Optional<Product> findByIdAndShopId(Long id, Long shopId);
}
