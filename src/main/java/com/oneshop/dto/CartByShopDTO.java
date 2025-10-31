package com.oneshop.dto;

import java.math.BigDecimal;
import java.util.List;

import com.oneshop.entity.CartItemEntity;
import com.oneshop.entity.Promotion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartByShopDTO {
    private Long shopId;
    private String shopName;
    private List<CartItemEntity> items;
    private BigDecimal subtotal;
    private List<Promotion> promotions; // Khuyến mãi của shop này
}
