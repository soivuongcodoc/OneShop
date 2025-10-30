package com.oneshop.repository;

import com.oneshop.entity.AdminPromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdminPromotionRepository extends JpaRepository<AdminPromotion, Long> {
    List<AdminPromotion> findAllByOrderByIdDesc();

    @Query("select p from AdminPromotion p " +
           "where lower(p.name) like lower(concat('%', :kw, '%')) " +
           "order by p.id desc")
    List<AdminPromotion> searchByName(@Param("kw") String keyword);
}
