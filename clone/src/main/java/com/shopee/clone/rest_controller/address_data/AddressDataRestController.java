package com.shopee.clone.rest_controller.address_data;

import com.shopee.clone.entity.address_data.AddressDataEntity;
import com.shopee.clone.service.address_data.AddressDataService;
import com.shopee.clone.service.address_data.impl.AddressDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/address-data")
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
}
