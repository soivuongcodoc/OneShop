package com.oneshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.oneshop.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUserId(Long userId);

    java.util.Optional<Address> findFirstByUserIdAndIsDefaultTrue(Long userId);

    @Transactional
    void deleteByUserId(Long userId);
}
