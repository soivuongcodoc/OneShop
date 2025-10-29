package com.oneshop.service.user;

import com.oneshop.entity.Review;

import java.util.List;

public interface ReviewService {

    List<Review> findByProduct(Long productId);

    Review create(Review review);

    boolean userPurchasedProduct(Long userId, Long productId);
}
