package com.shopee.clone.service.address;

import com.shopee.clone.entity.AddressEntity;
import com.shopee.clone.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public interface AddressService {
    AddressEntity save(AddressEntity addressEntity);
    List<AddressEntity> saveAll(List<AddressEntity> addressEntityList);
}
