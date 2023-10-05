package com.shopee.clone.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "product_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long pItemId;
    private Double price;
    private Integer qtyInStock;
    private Boolean status = true;

    @OneToMany(mappedBy = "productItem", fetch = FetchType.EAGER)
    private List<ImageProductEntity> imageProductList;

    @ManyToOne
    @JoinColumn(name = "productId")
    private ProductEntity product;

    @ManyToMany
    @JoinTable(
            name = "productitem_has_optiontype",
            joinColumns = @JoinColumn(name = "product_item_id"),
            inverseJoinColumns = @JoinColumn(name = "optiontype_id"))
    private Set<OptionTypeEntity> optionTypes = new HashSet<>();

    @OneToMany(mappedBy = "productItem", fetch = FetchType.EAGER)
    private List<OptionValueEntity> optionValues;
}
