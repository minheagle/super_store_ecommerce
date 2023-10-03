package com.shopee.clone.repository;

import com.shopee.clone.entity.AddressEntity;
import com.shopee.clone.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity,Long> {
    @Query("SELECT a FROM AddressEntity a WHERE a.user.id = :userId")
    List<AddressEntity> findByUserId(@Param("userId") Long userId);
}
