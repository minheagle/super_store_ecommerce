package com.shopee.clone.response.province;

import com.shopee.clone.entity.address_data.AddressDataEntity;
import com.shopee.clone.entity.address_data.DistrictEntity;
import com.shopee.clone.entity.address_data.WardEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DetailAddress {
    private AddressDataEntity province;
    private DistrictEntity district;
    private WardEntity ward;
}
