package com.shopee.clone.repository.cart;

import com.shopee.clone.entity.SellerEntity;
import com.shopee.clone.entity.UserEntity;
import com.shopee.clone.entity.cart.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CartRepository extends JpaRepository<CartEntity,Long> {
    List<CartEntity> findByUser_Id(Long userId);
    List<CartEntity> findByUser(UserEntity user);

    List<CartEntity> findByUserAndSeller(UserEntity user, SellerEntity seller);
}
