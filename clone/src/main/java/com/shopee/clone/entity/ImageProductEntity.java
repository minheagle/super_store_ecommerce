package com.shopee.clone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "image_product")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long imgProductId;

    @Column(name = "image_public_id")
    private String imgPublicId;

    @Column(name = "image_product_url")
    private String imgProductUrl;

    @ManyToOne
    @JoinColumn(name = "productItemId")
    private ProductItemEntity productItem;
}
