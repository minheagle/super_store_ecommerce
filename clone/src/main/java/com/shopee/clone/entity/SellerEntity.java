package com.shopee.clone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "sellers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "store_name")
    private String storeName;
    @Column(name = "store_address")
    private String storeAddress;
    @Column(name = "store_avatar_url")
    private String storeAvatarUrl;
    @Column(name = "store_background_url")
    private String storeBackgroundUrl;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "number_follower")
    private Integer numberFollower = 0;
}
