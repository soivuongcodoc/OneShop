package com.oneshop.controller.user;

import com.oneshop.entity.Review;
import com.oneshop.service.user.ReviewService;
import com.oneshop.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
import java.nio.file.*;

@Controller
@RequestMapping("/user/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public String add(@RequestParam Long productId,
            @RequestParam Integer rating,
            @RequestParam String comment,
            @RequestParam(name = "media", required = false) MultipartFile media) {
        var current = userService.getCurrentUser();
        if (current == null) {
            return "redirect:/auth/login";
        }
        if (comment == null || comment.trim().length() < 50) {
            return "redirect:/product/" + productId + "?error=comment_too_short";
        }
        if (!reviewService.userPurchasedProduct(current.getId(), productId)) {
            return "redirect:/product/" + productId + "?error=not_purchased";
        }
        String mediaUrl = null;
        if (media != null && !media.isEmpty()) {
            try {
                String filename = System.currentTimeMillis() + "_" + StringUtils.cleanPath(media.getOriginalFilename());
                Path uploadDir = Paths.get("uploads/reviews");
                Files.createDirectories(uploadDir);
                Path filePath = uploadDir.resolve(filename);
                Files.copy(media.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                mediaUrl = "/uploads/reviews/" + filename;
            } catch (Exception e) {
                // bỏ qua lỗi upload để không chặn review
            }
        }
        Review rv = Review.builder()
                .userId(current.getId())
                .product(com.oneshop.entity.Product.builder().id(productId).build())
                .rating(rating)
                .comment(comment.trim())
                .mediaUrl(mediaUrl)
                .build();
        reviewService.create(rv);
        return "redirect:/product/" + productId + "?success=review_added";
    }
}
