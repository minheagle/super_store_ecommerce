package com.shopee.clone.repository;

import com.shopee.clone.entity.OptionValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionValueRepository extends JpaRepository<OptionValueEntity,Long> {
}
