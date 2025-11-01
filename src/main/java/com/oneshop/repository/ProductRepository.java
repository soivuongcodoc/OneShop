package com.oneshop.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.oneshop.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByShopId(Long shopId);

    List<Product> findByShopIdAndActiveTrue(Long shopId);

    java.util.Optional<Product> findByIdAndShopId(Long id, Long shopId);

    List<Product> findTop10ByOrderByIdDesc();

    List<Product> findTop5ByOrderByCreatedAtDesc();

    List<Product> findTop10ByOrderBySoldDesc();

    List<Product> findTop5ByOrderBySoldDesc();

    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description, Pageable pageable);

    Page<Product> findByFeaturedTrue(Pageable pageable);

    Page<Product> findAllByOrderByIdDesc(Pageable pageable);

    Page<Product> findAllByOrderBySoldDesc(Pageable pageable);

    List<Product> findBySoldGreaterThanOrderBySoldDesc(int sold);

    @Query("SELECT p FROM Product p ORDER BY p.sold DESC")
    Page<Product> findTopRated(Pageable pageable);

    // Sửa: lấy sản phẩm được yêu thích - đơn giản hóa bằng cách dùng sold thay vì wishlist
    // Vì native query với GROUP BY không hoạt động tốt với Pageable
    default Page<Product> findMostFavorited(Pageable pageable) {
        // Tạm thời dùng sold thay vì wishlist count
        return findAllByOrderBySoldDesc(pageable);
    }

    // Method lấy top 20 yêu thích nhất (dùng native query)
    @Query(value = "SELECT TOP 20 p.* FROM products p "
        + "LEFT JOIN wishlists w ON w.product_id = p.id "
        + "GROUP BY p.id, p.shop_id, p.category_id, p.name, p.description, p.price, p.stock, "
        + "p.image_url, p.active, p.featured, p.sold, p.created_at "
        + "ORDER BY COUNT(w.id) DESC",
            nativeQuery = true)
    List<Product> findTop20MostFavorited();

    @Transactional
    void deleteByShopId(Long shopId);
}
