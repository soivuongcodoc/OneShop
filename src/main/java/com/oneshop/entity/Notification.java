package com.oneshop.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private NotificationType type;

    @Column(nullable = false, length = 500)
    private String message;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "shop_request_id")
    private Long shopRequestId;

    @Column(length = 200)
    private String title;

    @Column(length = 500)
    private String link;

    @Builder.Default
    @Column(name = "is_read")
    private boolean read = false;

    @Builder.Default
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum NotificationType {
        NEW_ORDER,                  // Vendor: Có đơn hàng mới
        ORDER_CONFIRMED,            // User: Đơn hàng được xác nhận
        ORDER_CANCELLED,            // User: Đơn hàng bị hủy
        PRODUCT_DELETED,            // Vendor: Sản phẩm bị xóa bởi admin
        SHOP_REQUEST_APPROVED,      // User: Yêu cầu shop được duyệt
        SHOP_REQUEST_REJECTED,      // User: Yêu cầu shop bị từ chối
        NEW_SHOP_REQUEST            // Admin: Có yêu cầu shop mới
    }
}
