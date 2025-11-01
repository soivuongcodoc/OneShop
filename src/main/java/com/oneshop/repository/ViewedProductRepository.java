package com.oneshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.oneshop.entity.ViewedProduct;

@Repository
public interface ViewedProductRepository extends JpaRepository<ViewedProduct, Long> {

    List<ViewedProduct> findByUserIdOrderByViewedAtDesc(Long userId);

    @Transactional
    void deleteByUserId(Long userId);
}
