package com.shopee.clone.service.address_data.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopee.clone.entity.address_data.AddressDataEntity;
import com.shopee.clone.entity.address_data.DistrictEntity;
import com.shopee.clone.entity.address_data.WardEntity;
import com.shopee.clone.repository.address_list.AddressDataRepository;
import com.shopee.clone.repository.address_list.DistrictRepository;
import com.shopee.clone.repository.address_list.WardRepository;
import com.shopee.clone.service.address_data.AddressDataService;
import com.shopee.clone.util.ResponseObject;
import jakarta.transaction.Transactional;
import com.fasterxml.jackson.core.type.TypeReference;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@Service
public class AddressDataServiceImpl implements AddressDataService {
    private final String API_URL = "https://provinces.open-api.vn/api/?depth=3";


    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private ModelMapper mapper;
    @Autowired
    private WardRepository wardRepository;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AddressDataRepository addressDataRepository;

    @Transactional
    @Override
    public ResponseEntity<?> fetchAndSaveAddressData() {
        try{
        String response = restTemplate.getForObject(API_URL, String.class);
        // Parse the JSON response into Address objects
        List<AddressDataEntity> addresses = parseResponse(response);
        // Save the addresses to the database
        addressDataRepository.saveAll(addresses);

        for (AddressDataEntity address : addresses) {
            // Lưu thông tin Districts
            for (DistrictEntity district : address.getDistrictEntities()) {
                district.setAddressData(address);
                districtRepository.save(district);

                // Lưu thông tin Wards trong mỗi District
                for (WardEntity ward : district.getWards()) {
                    ward.setDistrict(district);
                    wardRepository.save(ward);
                }
            }
        }

            return ResponseEntity.ok().body(ResponseObject
                    .builder()
                    .status("SUCCESS")
                    .message("Create Address Data in database!")
                    .results("")
                    .build()
            );
        }catch (Exception e){
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject
                            .builder()
                            .status("FAIL")
                            .message(e.getMessage())
                            .results("")
                            .build()
                    );
        }
    }
    @Override
    public List<AddressDataEntity> parseResponse(String response) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<AddressDataEntity> addresses = null;

        try {
            // Chuyển đổi JSON thành danh sách đối tượng Address
            addresses = objectMapper.readValue(response, new TypeReference<List<AddressDataEntity>>() {});
        } catch (IOException e) {
            e.printStackTrace();

        }
        return addresses;
    }
}
