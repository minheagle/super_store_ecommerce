package com.shopee.clone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long productId;
    private String productName;
    private Double minPrice;
    private Double maxPrice;
    private String description;
    private Boolean status = true;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private List<ProductItemEntity> productItemList;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private CategoryEntity category;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private SellerEntity seller;

    private Double voteStar;
}
