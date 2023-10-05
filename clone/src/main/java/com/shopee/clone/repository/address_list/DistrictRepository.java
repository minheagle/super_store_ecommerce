package com.shopee.clone.repository.address_list;

import com.shopee.clone.entity.address_data.AddressDataEntity;
import com.shopee.clone.entity.address_data.DistrictEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DistrictRepository extends JpaRepository<DistrictEntity,Long> {
    Optional<DistrictEntity> findByName(String name);
    Optional<DistrictEntity> findByNameAndAddressData_Id(String name, Long addressDataEntityId);
}
