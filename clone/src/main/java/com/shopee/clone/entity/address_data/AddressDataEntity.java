package com.shopee.clone.entity.address_data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "address_data")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @JsonIgnore
    @JsonProperty("districts")
    @OneToMany(mappedBy = "addressData")
    private List<DistrictEntity> districtEntities;
}