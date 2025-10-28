package com.oneshop.service.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oneshop.entity.Product;

public interface ProductService {

    List<Product> top10ByIdDesc();

    List<Product> top10BySoldDesc();

    Page<Product> findAll(Pageable pageable);

    Page<Product> listByCategory(Long categoryId, Pageable pageable);

    Page<Product> listNew(Pageable pageable);

    Page<Product> listBestSelling(Pageable pageable);

    Page<Product> listTopRated(Pageable pageable);

    Page<Product> listMostFavorited(Pageable pageable);

    Page<Product> search(String q, Pageable pageable);

    Optional<Product> findById(Long id);

    Page<Product> featured(Pageable pageable);

    List<Product> findByShopId(Long shopId);

    Optional<Product> findByIdAndShopId(Long id, Long shopId);

    // Lấy sản phẩm bán trên 10 sắp xếp theo sold giảm dần
    List<Product> findProductsSoldGreaterThan10();
}
