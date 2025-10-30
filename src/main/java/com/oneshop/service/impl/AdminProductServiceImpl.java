package com.oneshop.service.impl;

import com.oneshop.entity.Product;
import com.oneshop.repository.AdminProductRepository;
import com.oneshop.service.admin.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminProductServiceImpl implements ProductService {

    @Autowired
    private AdminProductRepository productRepo;

    @Override
    public List<Product> findAll() {
        return productRepo.findAllByOrderByIdDesc();
    }

    @Override
    public Product findById(Long id) {
        return productRepo.findById(id).orElse(null);
    }

    @Override
    public Product save(Product product) {
        return productRepo.save(product);
    }

    @Override
    public void delete(Long id) {
        productRepo.deleteById(id);
    }

    @Override
    public List<Product> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return productRepo.findAllByOrderByIdDesc();
        }
        return productRepo.findByNameContainingIgnoreCaseOrderByIdDesc(keyword);
      }
    }