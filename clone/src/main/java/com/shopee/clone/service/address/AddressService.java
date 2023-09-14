package com.shopee.clone.service.address;

import com.shopee.clone.entity.AddressEntity;
import com.shopee.clone.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface AddressService {
    AddressEntity save(AddressEntity addressEntity);
    void saveAll(List<AddressEntity> addressEntityList);
    List<AddressEntity> findByUserId(Long userId);

    Optional<AddressEntity> findById(Long id);
}
