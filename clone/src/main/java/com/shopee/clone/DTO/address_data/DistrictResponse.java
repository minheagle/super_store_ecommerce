package com.shopee.clone.DTO.address_data;

import lombok.Data;

import java.util.List;
@Data
public class DistrictResponse {
    private String district;
    private List<WardResponse> wardList;
}
