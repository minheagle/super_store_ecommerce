package com.shopee.clone.entity.address_data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "district")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DistrictEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @JsonIgnore
    @OneToMany(mappedBy = "district",fetch = FetchType.EAGER)
    private List<WardEntity> wards;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "address_data_id")
    private AddressDataEntity addressData;
}