package com.shopee.clone.service.address;

import com.shopee.clone.entity.AddressEntity;

import java.util.List;

public interface AddressService {
    AddressEntity save(AddressEntity addressEntity);
    List<AddressEntity> saveAll(List<AddressEntity> addressEntityList);
}
