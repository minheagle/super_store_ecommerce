package com.shopee.clone.repository.address_list;

import com.shopee.clone.entity.address_data.AddressDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressDataRepository extends JpaRepository<AddressDataEntity,Long> {
    Optional<AddressDataEntity> findByName(String name);
}
