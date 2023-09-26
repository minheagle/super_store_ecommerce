package com.shopee.clone.entity.order;

import com.shopee.clone.entity.ProductItemEntity;
import com.shopee.clone.entity.SellerEntity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "order_detail")
@Data
public class OrderDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private SellerEntity seller;
    @ManyToOne
    @JoinColumn(name = "product_items_id")
    private ProductItemEntity productItems;
    private Double unitPrice;
    private Integer quantity;
    private Double shipMoneyOnProduct;
}
