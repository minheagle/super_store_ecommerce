package com.shopee.clone.entity.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shopee.clone.entity.ProductItemEntity;
import com.shopee.clone.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
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
    @JoinColumn(name = "product_items_id")
    private ProductItemEntity productItems;
    private Integer quantity;
}
