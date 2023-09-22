package com.shopee.clone.service.address_data;

import com.shopee.clone.entity.address_data.AddressDataEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AddressDataService {
    List<AddressDataEntity> parseResponse(String response);
    ResponseEntity<?> fetchAndSaveAddressData();

    ResponseEntity<?> findCityById(Long id);
}
