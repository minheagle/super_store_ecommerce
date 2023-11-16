package com.shopee.clone.rest_controller.address_data;

import com.shopee.clone.DTO.address_data.GetAddressDetailRequest;
import com.shopee.clone.DTO.address_data.GetDistrictByNameRequest;
import com.shopee.clone.DTO.address_data.GetProvinceByNameRequest;
import com.shopee.clone.DTO.address_data.GetWardByNameRequest;
import com.shopee.clone.entity.address_data.AddressDataEntity;
import com.shopee.clone.service.address_data.AddressDataService;
import com.shopee.clone.service.address_data.impl.AddressDataServiceImpl;
import com.shopee.clone.util.ResponseObject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/63-provinces")
public class AddressDataRestController {
    @Autowired
    private AddressDataService addressDataService;
    @GetMapping("save")
    private ResponseEntity<?> saveData(){
        return addressDataService.fetchAndSaveAddressData();
    }
    @GetMapping("/get_city/{id}")
    private ResponseEntity<?> getCity(@PathVariable Long id){
        return addressDataService.findCityById(id);
    }

    @GetMapping("/provinces")
    public ResponseEntity<?> getAllProvince(){
        return addressDataService.getAllProvince();
    }

    @PostMapping("/provinces/by-name")
    public ResponseEntity<?> getProvinceByName(@RequestBody @Valid GetProvinceByNameRequest getProvinceByNameRequest){
        return addressDataService.getProvinceByName(getProvinceByNameRequest.getName());
    }

    @GetMapping("/districts/{provinceId}")
    public ResponseEntity<?> getAllDistrictByProvinceId(@PathVariable("provinceId") @NotBlank Long provinceId){
        return addressDataService.getAllDistrictByProvinceId(provinceId);
    }

    @PostMapping("/districts/{provinceId}/by-name")
    public ResponseEntity<?> getDistrictByName(@PathVariable("provinceId") @NotBlank Object provinceId,
                                               @RequestBody @Valid GetDistrictByNameRequest getDistrictByNameRequest){
        Long idLong = Long.valueOf((String) provinceId);
        if (idLong <= 0) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("FAIL")
                                    .message("Province not found")
                                    .results("")
                                    .build()
                    );
        }
        return addressDataService.getDistrictByName(idLong, getDistrictByNameRequest.getName());
    }

    @GetMapping("/wards/{districtId}")
    public ResponseEntity<?> getAllWardByDistrictId(@PathVariable("districtId") @NotBlank Long districtId){
        return addressDataService.getAllWardByDistrictId(districtId);
    }

    @PostMapping("/wards/by-name")
    public ResponseEntity<?> getWardByName(@RequestBody @Valid GetWardByNameRequest getWardByNameRequest){
        return addressDataService.getWardByName(getWardByNameRequest.getName());
    }

    @PostMapping("/address-detail")
    public ResponseEntity<?> getAddressDetail(@RequestBody @Valid GetAddressDetailRequest getAddressDetailRequest){
        return addressDataService.getDetailAddress(getAddressDetailRequest);
    }
}
