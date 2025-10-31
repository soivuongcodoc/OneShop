package com.oneshop.controller;

import com.oneshop.dto.NotificationDTO;
import com.oneshop.entity.Notification;
import com.oneshop.security.AuthFacade;
import com.oneshop.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    
    private final NotificationService notificationService;
    private final AuthFacade authFacade;

    /**
     * Lấy danh sách thông báo gần đây (10 mới nhất)
     */
    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getNotifications() {
        Long userId = authFacade.requireUserId();
        List<Notification> notifications = notificationService.getRecentNotifications(userId);
        
        List<NotificationDTO> dtos = notifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(dtos);
    }

    /**
     * Lấy số lượng thông báo chưa đọc
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getUnreadCount() {
        Long userId = authFacade.requireUserId();
        long count = notificationService.getUnreadCount(userId);
        
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Đánh dấu một thông báo đã đọc
     */
    @PutMapping("/{id}/read")
    public ResponseEntity<Map<String, String>> markAsRead(@PathVariable Long id) {
        Long userId = authFacade.requireUserId();
        notificationService.markAsRead(id, userId);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Đã đánh dấu thông báo đã đọc");
        
        return ResponseEntity.ok(response);
    }

    /**
     * Đánh dấu tất cả thông báo đã đọc
     */
    @PutMapping("/read-all")
    public ResponseEntity<Map<String, String>> markAllAsRead() {
        Long userId = authFacade.requireUserId();
        notificationService.markAllAsRead(userId);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Đã đánh dấu tất cả thông báo đã đọc");
        
        return ResponseEntity.ok(response);
    }

    /**
     * Helper: Convert entity to DTO
     */
    private NotificationDTO convertToDTO(Notification notification) {
        return NotificationDTO.builder()
                .id(notification.getId())
                .type(notification.getType().name())
                .message(notification.getMessage())
                .orderId(notification.getOrderId())
                .productId(notification.getProductId())
                .read(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
