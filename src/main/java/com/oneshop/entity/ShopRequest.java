package com.oneshop.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "shop_requests")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ShopRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 200)
    private String shopName;

    @Column(length = 1000)
    private String description;

    @Column(length = 300)
    private String address;

    @Column(length = 50)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private RequestStatus status = RequestStatus.PENDING;

    @Column(length = 500)
    private String rejectReason;

    @Builder.Default
    private LocalDateTime requestDate = LocalDateTime.now();

    private LocalDateTime processedDate;

    @ManyToOne
    @JoinColumn(name = "processed_by")
    private User processedBy; // Admin who processed

    public enum RequestStatus {
        PENDING,    // Chờ duyệt
        APPROVED,   // Đã duyệt
        REJECTED    // Từ chối
    }
}
