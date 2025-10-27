package com.oneshop.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "shops")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Shop {
    @Id
    private Long id; // same as vendor user's id

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private User vendor; // vendor user

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(length = 300)
    private String address;

    @Column(length = 50)
    private String phone;

    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;
}
