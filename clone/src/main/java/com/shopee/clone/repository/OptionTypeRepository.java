package com.shopee.clone.repository;

import com.shopee.clone.entity.OptionTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionTypeRepository extends JpaRepository<OptionTypeEntity,Long> {
}
