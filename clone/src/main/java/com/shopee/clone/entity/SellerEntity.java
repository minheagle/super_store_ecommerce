package com.shopee.clone.entity;

import com.shopee.clone.entity.order.OrderEntity;
import com.shopee.clone.entity.promotion.PromotionEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "sellers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "store_name")
    private String storeName;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "store_address")
    private String storeAddress;

    @Column(name = "img_avatar_public_id")
    private String imgAvatarPublicId;
    @Column(name = "store_avatar_url")
    private String storeAvatarUrl;

    @Column(name = "img_background_public_id")
    private String imgBackgroundPublicId;
    @Column(name = "store_background_url")
    private String storeBackgroundUrl;

    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "number_follower")
    private Integer numberFollower = 0;
    @Column(name = "store_phone_number")
    private String storePhoneNumber;
    @Column(name = "store_bank_name")
    private String storeBankName;
    @Column(name = "store_bank_account_number")
    private String storeBankAccountNumber;
    @Column(name = "chat_id")
    private String chatId;
    @OneToMany(mappedBy = "seller", fetch = FetchType.EAGER)
    private List<ProductEntity> productList;
    @OneToMany(mappedBy = "seller_created", fetch = FetchType.EAGER)
    private List<PromotionEntity> promotions;
}
