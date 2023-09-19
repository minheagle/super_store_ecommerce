package com.shopee.clone.DTO.address_data;

import lombok.Data;

import java.util.List;
@Data
public class AddressDataResponse {
    private String city;
    private List<DistrictResponse> districtList;
}
