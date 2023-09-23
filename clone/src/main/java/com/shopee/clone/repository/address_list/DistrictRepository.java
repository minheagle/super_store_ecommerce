package com.shopee.clone.repository.address_list;

import com.shopee.clone.entity.address_data.DistrictEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepository extends JpaRepository<DistrictEntity,Long> {
}
