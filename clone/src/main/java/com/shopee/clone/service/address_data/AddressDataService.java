package com.shopee.clone.service.address_data;

import com.shopee.clone.DTO.address_data.GetAddressDetailRequest;
import com.shopee.clone.entity.address_data.AddressDataEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AddressDataService {
    List<AddressDataEntity> parseResponse(String response);
    ResponseEntity<?> fetchAndSaveAddressData();
    ResponseEntity<?> getAllProvince();
    ResponseEntity<?> getProvinceByName(String provinceName);
    ResponseEntity<?> getAllDistrictByProvinceId(Long provinceId);
    ResponseEntity<?> getDistrictByName(Long provinceId, String districtName);
    ResponseEntity<?> getAllWardByDistrictId(Long districtId);
    ResponseEntity<?> getWardByName(String wardName);
    ResponseEntity<?> findCityById(Long id);
    ResponseEntity<?> getDetailAddress(GetAddressDetailRequest getAddressDetailRequest);

}
