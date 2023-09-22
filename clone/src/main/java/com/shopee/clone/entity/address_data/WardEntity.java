package com.shopee.clone.entity.address_data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "ward")
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class WardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
//    private Integer code;
//    private String codename;
//    @JsonProperty("division_type")
//    private String divisionType;
//    @JsonProperty("short_codename")
//    private String shortCodename;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "district_id")
    private DistrictEntity district;
 }
