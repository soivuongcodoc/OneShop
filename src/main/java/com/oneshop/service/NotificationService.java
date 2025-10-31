package com.oneshop.service;

import com.oneshop.entity.Notification;
import com.oneshop.entity.Notification.NotificationType;
import com.oneshop.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    
    private final NotificationRepository notificationRepo;

    /**
     * Tạo thông báo đơn hàng mới cho vendor
     */
    public void createNewOrderNotification(Long vendorUserId, Long orderId, String orderCode) {
        Notification notification = Notification.builder()
                .userId(vendorUserId)
                .type(NotificationType.NEW_ORDER)
                .message("Bạn có đơn hàng mới #" + orderCode)
                .orderId(orderId)
                .build();
        notificationRepo.save(notification);
    }

    /**
     * Tạo thông báo đơn hàng được xác nhận cho user
     */
    public void createOrderConfirmedNotification(Long userId, Long orderId, String orderCode) {
        Notification notification = Notification.builder()
                .userId(userId)
                .type(NotificationType.ORDER_CONFIRMED)
                .message("Đơn hàng #" + orderCode + " đã được xác nhận")
                .orderId(orderId)
                .build();
        notificationRepo.save(notification);
    }

    /**
     * Tạo thông báo đơn hàng bị hủy cho user
     */
    public void createOrderCancelledNotification(Long userId, Long orderId, String orderCode) {
        Notification notification = Notification.builder()
                .userId(userId)
                .type(NotificationType.ORDER_CANCELLED)
                .message("Đơn hàng #" + orderCode + " đã bị hủy")
                .orderId(orderId)
                .build();
        notificationRepo.save(notification);
    }

    /**
     * Tạo thông báo sản phẩm bị xóa cho vendor
     */
    public void createProductDeletedNotification(Long vendorUserId, Long productId, String productName) {
        Notification notification = Notification.builder()
                .userId(vendorUserId)
                .type(NotificationType.PRODUCT_DELETED)
                .title("Sản phẩm bị xóa")
                .message("Sản phẩm '" + productName + "' đã bị xóa bởi quản trị viên")
                .productId(productId)
                .link("/vendor/products")
                .build();
        notificationRepo.save(notification);
    }

    /**
     * Tạo thông báo yêu cầu shop được duyệt
     */
    public void createShopRequestApprovedNotification(Long userId, Long shopRequestId, String shopName) {
        Notification notification = Notification.builder()
                .userId(userId)
                .type(NotificationType.SHOP_REQUEST_APPROVED)
                .title("Yêu cầu mở shop được duyệt!")
                .message("Chúc mừng! Yêu cầu mở shop '" + shopName + "' của bạn đã được duyệt. Bạn có thể bắt đầu quản lý shop ngay bây giờ.")
                .shopRequestId(shopRequestId)
                .link("/vendor/dashboard")
                .build();
        notificationRepo.save(notification);
    }

    /**
     * Tạo thông báo yêu cầu shop bị từ chối
     */
    public void createShopRequestRejectedNotification(Long userId, Long shopRequestId, String shopName, String reason) {
        Notification notification = Notification.builder()
                .userId(userId)
                .type(NotificationType.SHOP_REQUEST_REJECTED)
                .title("Yêu cầu mở shop bị từ chối")
                .message("Rất tiếc! Yêu cầu mở shop '" + shopName + "' của bạn đã bị từ chối. Lý do: " + reason)
                .shopRequestId(shopRequestId)
                .link("/user/shop-request")
                .build();
        notificationRepo.save(notification);
    }

    /**
     * Tạo thông báo cho admin khi có yêu cầu shop mới
     */
    public void createNewShopRequestNotification(Long adminUserId, Long shopRequestId, String username, String shopName) {
        Notification notification = Notification.builder()
                .userId(adminUserId)
                .type(NotificationType.NEW_SHOP_REQUEST)
                .title("Yêu cầu mở shop mới")
                .message("User '" + username + "' đã gửi yêu cầu mở shop với tên '" + shopName + "'. Vui lòng kiểm tra và duyệt.")
                .shopRequestId(shopRequestId)
                .link("/admin/shop-requests")
                .build();
        notificationRepo.save(notification);
    }

    /**
     * Lấy tất cả thông báo của user (giới hạn 10 mới nhất)
     */
    public List<Notification> getRecentNotifications(Long userId) {
        return notificationRepo.findTop10ByUserIdOrderByCreatedAtDesc(userId);
    }

    /**
     * Lấy số lượng thông báo chưa đọc
     */
    public long getUnreadCount(Long userId) {
        return notificationRepo.countByUserIdAndReadFalse(userId);
    }

    /**
     * Đánh dấu thông báo đã đọc
     */
    @Transactional
    public void markAsRead(Long notificationId, Long userId) {
        notificationRepo.findById(notificationId).ifPresent(notification -> {
            if (notification.getUserId().equals(userId)) {
                notification.setRead(true);
                notificationRepo.save(notification);
            }
        });
    }

    /**
     * Đánh dấu tất cả thông báo đã đọc
     */
    @Transactional
    public void markAllAsRead(Long userId) {
        List<Notification> unreadNotifications = notificationRepo.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId);
        unreadNotifications.forEach(n -> n.setRead(true));
        notificationRepo.saveAll(unreadNotifications);
    }
}
