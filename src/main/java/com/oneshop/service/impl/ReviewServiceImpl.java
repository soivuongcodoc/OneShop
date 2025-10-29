package com.oneshop.service.impl;

import com.oneshop.entity.Review;
import com.oneshop.repository.ReviewRepository;
import com.oneshop.repository.OrderDetailRepository;
import com.oneshop.service.user.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public List<Review> findByProduct(Long productId) {
        return reviewRepository.findByProductIdOrderByCreatedAtDesc(productId);
    }

    @Override
    public Review create(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    public boolean userPurchasedProduct(Long userId, Long productId) {
        return orderDetailRepository.existsPurchasedByUserAndProduct(userId, productId);
    }
}
