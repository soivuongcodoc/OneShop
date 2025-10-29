package com.oneshop.repository;

import com.oneshop.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    List<Promotion> findByShopIdOrderByStartTimeDesc(Long shopId);
    List<Promotion> findByShopIdAndActiveTrueAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(Long shopId, LocalDateTime start, LocalDateTime end);
}
