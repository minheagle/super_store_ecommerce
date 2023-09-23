package com.shopee.clone.repository.product;

import com.shopee.clone.entity.OptionTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionTypeRepository extends JpaRepository<OptionTypeEntity,Long> {
}
