package com.shopee.clone.entity.address_data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "address_data")
public class AddressDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer code;
    private String codename;
    @JsonIgnoreProperties(ignoreUnknown = true)
    private String divisionType;
    private Integer phoneCode;
    @OneToMany(mappedBy = "addressData",fetch = FetchType.EAGER)
    private List<DistrictEntity> districtEntities;
}