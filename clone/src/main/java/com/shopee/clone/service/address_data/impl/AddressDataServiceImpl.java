package com.shopee.clone.service.address_data.impl;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.shopee.clone.DTO.address_data.GetAddressDetailRequest;
import com.shopee.clone.entity.address_data.AddressDataEntity;
import com.shopee.clone.entity.address_data.DistrictEntity;
import com.shopee.clone.entity.address_data.WardEntity;
import com.shopee.clone.repository.address_list.AddressDataRepository;
import com.shopee.clone.repository.address_list.DistrictRepository;
import com.shopee.clone.repository.address_list.WardRepository;
import com.shopee.clone.response.province.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

//    @Transactional
//    @Override
//    public ResponseEntity<?> fetchAndSaveAddressData() {
//        try{
//        String response = restTemplate.getForObject(API_URL, String.class);
//        // Parse the JSON response into Address objects
//        List<AddressDataEntity> addresses = parseResponse(response);
//        // Save the addresses to the database
//        addressDataRepository.saveAll(addresses);
//
//        for (AddressDataEntity address : addresses) {
//            // Lưu thông tin Districts
//            for (DistrictEntity district : address.getDistrictEntities()) {
//                district.setAddressData(address);
//                districtRepository.save(district);
//
//                // Lưu thông tin Wards trong mỗi District
//                for (WardEntity ward : district.getWards()) {
//                    ward.setDistrict(district);
//                    wardRepository.save(ward);
//                }
//            }
//        }
//
//            return ResponseEntity.ok().body(ResponseObject
//                    .builder()
//                    .status("SUCCESS")
//                    .message("Create Address Data in database!")
//                    .results("")
//                    .build()
//            );
//        }catch (Exception e){
//            return ResponseEntity
//                    .badRequest()
//                    .body(ResponseObject
//                            .builder()
//                            .status("FAIL")
//                            .message(e.getMessage())
//                            .results("")
//                            .build()
//                    );
//        }
//    }

    @Override
    public ResponseEntity<?> getAllProvince() {
        try{
            List<AddressDataEntity> listAllProvince = addressDataRepository.findAll();
            ListAllProvinceResponse<List<AddressDataEntity>> listListAllProvinceResponse = new ListAllProvinceResponse<>();
            listListAllProvinceResponse.setData(listAllProvince);
            listListAllProvinceResponse.setCount(listAllProvince.size());
            if (listAllProvince.size() == 0){
                return ResponseEntity
                        .ok()
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("List all province is empty")
                                        .results(listListAllProvinceResponse)
                                        .build()
                        );
            }
            return ResponseEntity
                    .ok()
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .message("Get list all province success")
                                    .results(listListAllProvinceResponse)
                                    .build()
                    );
        }catch (Exception e){
            return ResponseEntity
                    .badRequest()
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("FAIL")
                                    .message(e.getMessage())
                                    .results("")
                                    .build()
                    );
        }
    }

    @Override
    public ResponseEntity<?> getProvinceByName(String provinceName) {
        try{
            AddressDataEntity addressDataEntity = addressDataRepository.findByName(provinceName)
                    .orElseThrow(() -> new RuntimeException("Province not found"));
            GetAddressByNameResponse<AddressDataEntity> response = new GetAddressByNameResponse<>();
            response.setData(addressDataEntity);
            return ResponseEntity
                    .ok()
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .message("Get province success")
                                    .results(response)
                                    .build()
                    );
        }catch (Exception e){
            return ResponseEntity
                    .badRequest()
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("FAIL")
                                    .message(e.getMessage())
                                    .results("")
                                    .build()
                    );
        }
    }

    @Override
    public ResponseEntity<?> getAllDistrictByProvinceId(Long provinceId) {
        try{
            if(!addressDataRepository.existsById(provinceId)){
                return ResponseEntity
                        .badRequest()
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("FAIL")
                                        .message("Province not found")
                                        .results("")
                                        .build()
                        );
            }
            AddressDataEntity addressDataEntity = addressDataRepository.findById(provinceId)
                    .orElseThrow(() -> new RuntimeException("Province not found"));
            List<DistrictEntity> districtEntityList = addressDataEntity.getDistrictEntities();
            ListAllDistrictByProvinceResponse<List<DistrictEntity>> listAllDistrictByProvinceResponse = new ListAllDistrictByProvinceResponse<>();
            listAllDistrictByProvinceResponse.setData(districtEntityList);
            listAllDistrictByProvinceResponse.setCount(districtEntityList.size());
            if(districtEntityList.size() == 0){
                return ResponseEntity
                        .ok()
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("List district is empty")
                                        .results(listAllDistrictByProvinceResponse)
                                        .build()
                        );
            }

            return ResponseEntity
                    .ok()
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .message("Get list all district by province success")
                                    .results(listAllDistrictByProvinceResponse)
                                    .build()
                    );
        }catch (Exception e){
            return ResponseEntity
                    .badRequest()
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("FAIL")
                                    .message(e.getMessage())
                                    .results("")
                                    .build()
                    );
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> getDistrictByName(Long provinceId, String districtName) {
        try{
            AddressDataEntity addressDataEntity = addressDataRepository.findById(provinceId)
                    .orElseThrow(() -> new RuntimeException("Province not found"));
            DistrictEntity districtEntity = districtRepository.findByNameAndAddressData_Id(districtName, addressDataEntity.getId())
                    .orElseThrow(() -> new RuntimeException("District not found"));
            GetAddressByNameResponse<DistrictEntity> response = new GetAddressByNameResponse<>();
            response.setData(districtEntity);
            return ResponseEntity
                    .ok()
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .message("Get district success")
                                    .results(response)
                                    .build()
                    );
        }catch (Exception e){
            return ResponseEntity
                    .badRequest()
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("FAIL")
                                    .message(e.getMessage())
                                    .results("")
                                    .build()
                    );
        }
    }

    @Override
    public ResponseEntity<?> getAllWardByDistrictId(Long districtId) {
        try {
            if(!districtRepository.existsById(districtId)){
                return ResponseEntity
                        .badRequest()
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("FAIL")
                                        .message("District not found")
                                        .results("")
                                        .build()
                        );
            }
            DistrictEntity districtEntity = districtRepository.findById(districtId)
                    .orElseThrow(() -> new RuntimeException("District not found"));
            List<WardEntity> wardEntityList = districtEntity.getWards();
            ListAllWardByDistrictResponse<List<WardEntity>> listListAllWardByDistrictResponse = new ListAllWardByDistrictResponse<>();
            listListAllWardByDistrictResponse.setData(wardEntityList);
            listListAllWardByDistrictResponse.setCount(wardEntityList.size());
            if(wardEntityList.size() == 0){
                return ResponseEntity
                        .ok()
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("List ward is empty")
                                        .results(listListAllWardByDistrictResponse)
                                        .build()
                        );
            }
            return ResponseEntity
                    .ok()
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .message("Get list all ward by district success")
                                    .results(listListAllWardByDistrictResponse)
                                    .build()
                    );
        }catch (Exception e){
            return ResponseEntity
                    .badRequest()
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("FAIL")
                                    .message(e.getMessage())
                                    .results("")
                                    .build()
                    );
        }
    }

    @Override
    public ResponseEntity<?> getWardByName(String wardName) {
        try {
            WardEntity wardEntity = wardRepository.findByName(wardName)
                    .orElseThrow(() -> new RuntimeException("Ward not found"));
            GetAddressByNameResponse<WardEntity> response = new GetAddressByNameResponse<>();
            response.setData(wardEntity);
            return ResponseEntity
                    .ok()
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .message("Get ward success")
                                    .results(response)
                                    .build()
                    );
        }catch (Exception e){
            return ResponseEntity
                    .badRequest()
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("FAIL")
                                    .message(e.getMessage())
                                    .results("")
                                    .build()
                    );
        }
    }

    @Override
    public ResponseEntity<?> findCityById(Long id) {
        try{

            // Save the addresses to the database
            Optional<AddressDataEntity> address =  addressDataRepository.findById(id);

            if (address.isPresent()){
                AddressDataEntity addressData = mapper.map(address,AddressDataEntity.class);
                return ResponseEntity.ok().body(ResponseObject
                        .builder()
                        .status("SUCCESS")
                        .message("Create Address Data in database!")
                        .results(addressData)
                        .build()
                );
            }
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject
                            .builder()
                            .status("FAIL")
                            .message("Address not find!")
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
    public ResponseEntity<?> getDetailAddress(GetAddressDetailRequest getAddressDetailRequest) {
        try{
            AddressDataEntity addressDataEntity = addressDataRepository.findByName(getAddressDetailRequest.getProvinceName())
                    .orElseThrow(() -> new RuntimeException("Province not found"));
            DistrictEntity districtEntity = districtRepository.findByNameAndAddressData_Id(
                    getAddressDetailRequest.getDistrictName(),
                            addressDataEntity.getId()
                    )
                    .orElseThrow(() -> new RuntimeException("District not found"));
            WardEntity wardEntity = wardRepository.findByNameAndDistrict_Id(
                    getAddressDetailRequest.getWardName(),
                    districtEntity.getId())
                    .orElseThrow(() -> new RuntimeException("Ward not found"));
            GetDetailAddressResponse<DetailAddress> response = new GetDetailAddressResponse<>();
            response.setData(
                    DetailAddress
                            .builder()
                            .province(addressDataEntity)
                            .district(districtEntity)
                            .ward(wardEntity)
                            .build()
            );
            return ResponseEntity
                    .ok()
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .message("Get address detail success")
                                    .results(response)
                                    .build()
                    );
        }catch (Exception e){
            return ResponseEntity
                    .badRequest()
                    .body(
                            ResponseObject
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
