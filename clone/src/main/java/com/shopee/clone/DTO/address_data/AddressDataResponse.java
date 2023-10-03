package com.shopee.clone.DTO.address_data;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class AddressDataResponse {
    private String city;
    private List<DistrictResponse> districtList;
}
