package com.shopee.clone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "product_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pItemId;
    private Double price;
    private Integer qtyInStock;
    private Boolean status = true;

    @OneToMany(mappedBy = "productItem", fetch = FetchType.EAGER)
    private List<ImageProductEntity> imageProductList;

    @ManyToOne
    @JoinColumn(name = "productId")
    private ProductEntity product;

    @OneToMany(mappedBy = "productItem", fetch = FetchType.EAGER)
    private List<OptionTypeEntity> optionTypeList;
}
