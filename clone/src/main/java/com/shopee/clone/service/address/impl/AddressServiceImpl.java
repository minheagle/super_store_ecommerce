package com.shopee.clone.service.address.impl;

import com.shopee.clone.entity.AddressEntity;
import com.shopee.clone.repository.AddressRepository;
import com.shopee.clone.service.address.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressRepository addressRepository;
    @Override
    public AddressEntity save(AddressEntity addressEntity) {
        return addressRepository.save(addressEntity);
    }

    @Override
    public List<AddressEntity> saveAll(List<AddressEntity> addressEntityList) {
        return addressRepository.saveAll(addressEntityList);
    }
}
