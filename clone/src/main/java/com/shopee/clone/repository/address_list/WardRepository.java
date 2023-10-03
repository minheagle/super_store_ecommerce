package com.shopee.clone.repository.address_list;

import com.shopee.clone.entity.address_data.WardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WardRepository extends JpaRepository<WardEntity,Long> {
    Optional<WardEntity> findByName(String name);
    Optional<WardEntity> findByNameAndDistrict_Id(String name, Long districtId);
}
