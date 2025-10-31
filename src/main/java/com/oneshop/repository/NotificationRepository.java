package com.oneshop.repository;

import com.oneshop.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    List<Notification> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);
    
    long countByUserIdAndReadFalse(Long userId);
    
    List<Notification> findByUserIdAndReadFalseOrderByCreatedAtDesc(Long userId);
}
