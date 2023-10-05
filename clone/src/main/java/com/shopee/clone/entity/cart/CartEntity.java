package com.shopee.clone.entity.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shopee.clone.entity.ProductItemEntity;
import com.shopee.clone.entity.SellerEntity;
import com.shopee.clone.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart")
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private SellerEntity seller;
    @ManyToOne
    @JoinColumn(name = "product_items_id")
    private ProductItemEntity productItems;
    private Integer quantity;
}
