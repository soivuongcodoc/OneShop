package com.oneshop.service.admin;

import com.oneshop.entity.Product;
import java.util.List;

public interface ProductService {
    List<Product> findAll();
    Product findById(Long id);
    Product save(Product product);
    void delete(Long id);

    // 🔍 Thêm chức năng tìm kiếm theo tên (dành cho trang admin)
    List<Product> search(String keyword);
}
