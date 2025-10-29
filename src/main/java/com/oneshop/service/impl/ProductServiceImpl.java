package com.oneshop.service.impl;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.oneshop.entity.Product;
import com.oneshop.repository.ProductRepository;
import com.oneshop.service.user.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> top10ByIdDesc() {
        return productRepository.findTop10ByOrderByIdDesc();
    }

    @Override
    public List<Product> top10BySoldDesc() {
        return productRepository.findTop10ByOrderBySoldDesc();
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> listByCategory(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable);
    }

    @Override
    public Page<Product> search(String q, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(q, q, pageable);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Page<Product> featured(Pageable pageable) {
        return productRepository.findByFeaturedTrue(pageable);
    }

    @Override
    public Page<Product> listNew(Pageable pageable) {
        return productRepository.findAllByOrderByIdDesc(pageable);
    }

    @Override
    public Page<Product> listBestSelling(Pageable pageable) {
        return productRepository.findAllByOrderBySoldDesc(pageable);
    }

    @Override
    public Page<Product> listTopRated(Pageable pageable) {
        return productRepository.findTopRated(pageable);
    }

    @Override
    public Page<Product> listMostFavorited(Pageable pageable) {
        return productRepository.findMostFavorited(pageable);
    }

    @Override
    public List<Product> findByShopId(Long shopId) {
        return productRepository.findByShopId(shopId);
    }

    @Override
    public Optional<Product> findByIdAndShopId(Long id, Long shopId) {
        return productRepository.findByIdAndShopId(id, shopId);
    }

    @Override
    public List<Product> findProductsSoldGreaterThan10() {
        return productRepository.findBySoldGreaterThanOrderBySoldDesc(10);
    }
}
