package com.shopee.clone.DTO.address_data;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class DistrictResponse {
    private String district;
    private List<WardResponse> wardList;
}
