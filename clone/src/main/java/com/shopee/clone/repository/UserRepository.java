package com.shopee.clone.repository;

import com.shopee.clone.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u FROM UserEntity u " +
            "WHERE u.fullName LIKE %:keyword% " +
            "OR u.email LIKE %:keyword% " +
            "OR u.phone LIKE %:keyword% " +
            "OR u.userName LIKE %:keyword%")
    Page<UserEntity> searchUsers(@Param("keyword") String keyword, Pageable pageable);
    Optional<UserEntity> findByUserNameOrEmailOrPhone (String userName, String email, String phone);
    Optional<UserEntity> findByUserName(String userName);

    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);


}
