package com.shopee.clone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ManyToMany(mappedBy = "optionTypes")
    private Set<ProductItemEntity> productItems = new HashSet<>();

    @OneToMany(mappedBy = "optionType", fetch = FetchType.EAGER)
    private List<OptionValueEntity> optionValueList;
}
