package com.oneshop.repository;

import com.oneshop.entity.ShopRequest;
import com.oneshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShopRequestRepository extends JpaRepository<ShopRequest, Long> {
    Optional<ShopRequest> findByUserAndStatus(User user, ShopRequest.RequestStatus status);
    List<ShopRequest> findByStatus(ShopRequest.RequestStatus status);
    List<ShopRequest> findByUserOrderByRequestDateDesc(User user);
    
    @Transactional
    void deleteByUser(User user);
}
