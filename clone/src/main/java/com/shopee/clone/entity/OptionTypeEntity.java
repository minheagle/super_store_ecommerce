package com.shopee.clone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "option_types")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long opTypeId;
    private String optionName;

    @ManyToOne
    @JoinColumn(name = "product_item_id")
    private ProductItemEntity productItem;

    @OneToMany(mappedBy = "optionType", fetch = FetchType.EAGER)
    private List<OptionValueEntity> optionValueList;
}
