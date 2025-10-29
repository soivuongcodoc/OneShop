package com.oneshop.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shop_id", nullable = false)
    private Long shopId;

<<<<<<< HEAD
    @Column(name = "customer_id", nullable = false)
    private Long customerId;
=======
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
>>>>>>> admin

    @Builder.Default
    @Column(name = "order_date")
    private LocalDateTime orderDate = LocalDateTime.now();

    @Builder.Default
<<<<<<< HEAD
    @Column(nullable = false, length = 30)
    private String status = "new";
=======
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrderStatus status = OrderStatus.PENDING;
>>>>>>> admin

    @Column(name = "total_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal totalAmount;

<<<<<<< HEAD
    @Column(name = "payment_method", nullable = false, length = 50)
    private String paymentMethod;
=======
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id", nullable = false)
    private PaymentMethod paymentMethod;
>>>>>>> admin

    @Column(name = "shipping_address", nullable = false, length = 500)
    private String shippingAddress;
}
