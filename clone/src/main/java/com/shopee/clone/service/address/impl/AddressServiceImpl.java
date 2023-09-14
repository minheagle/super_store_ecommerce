package com.shopee.clone.service.address.impl;

import com.shopee.clone.entity.AddressEntity;
import com.shopee.clone.entity.UserEntity;
import com.shopee.clone.repository.AddressRepository;
import com.shopee.clone.service.address.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressRepository addressRepository;
    @Override
    public AddressEntity save(AddressEntity addressEntity) {
        return addressRepository.save(addressEntity);
    }

    @Override
    public void saveAll(List<AddressEntity> addressEntityList) {
        addressRepository.saveAll(addressEntityList);
    }

    @Override
    public List<AddressEntity> findByUserId (Long userId) {
        return  addressRepository.findByUserId(userId);
    }

    @Override
    public Optional<AddressEntity> findById(Long id) {
        return addressRepository.findById(id);
    }


}
