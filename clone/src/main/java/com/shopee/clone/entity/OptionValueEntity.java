package com.shopee.clone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "option_values")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionValueEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long opValueId;
    private String valueName;
//    private Double percent_price;

    @ManyToOne
    @JoinColumn(name = "option_type_id")
    private OptionTypeEntity optionType;

    @ManyToOne
    @JoinColumn(name = "product_item_id")
    private  ProductItemEntity productItem;
}
