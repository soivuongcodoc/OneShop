package com.oneshop.dto.admin;

import com.oneshop.entity.AdminPromotionDiscountType;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AdminPromotionForm {
    @NotBlank
    private String name;

    private String description;

    @NotNull
    private AdminPromotionDiscountType discountType = AdminPromotionDiscountType.PERCENTAGE;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal discountValue;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endTime;

    private Boolean active = Boolean.TRUE;
}
