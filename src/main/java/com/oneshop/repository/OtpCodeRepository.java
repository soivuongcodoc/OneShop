package com.oneshop.repository;

import com.oneshop.entity.OtpCode;
import com.oneshop.entity.OtpCode.OtpType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpCodeRepository extends JpaRepository<OtpCode, Long> {
  Optional<OtpCode> findTopByEmailAndTypeAndUsedFalseOrderByIdDesc(String email, OtpType type);
}
