package com.oneshop.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment_methods")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String name; // COD, BANK_TRANSFER, MOMO, ZALOPAY, etc.

    @Column(nullable = false, length = 100)
    private String displayName; // "Thanh toán khi nhận hàng", etc.

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;
}
