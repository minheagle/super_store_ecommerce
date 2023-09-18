package com.shopee.clone.entity.address_data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "district")
public class DistrictEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer code;
    private String codename;
    @JsonIgnoreProperties(ignoreUnknown = true)
    private String divisionType;
    private String shortCodename;
    @OneToMany(mappedBy = "district",fetch = FetchType.EAGER)
    private List<WardEntity> wards;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "address_data_id")
    private AddressDataEntity addressData;
}