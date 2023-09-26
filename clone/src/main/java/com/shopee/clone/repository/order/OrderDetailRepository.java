package com.shopee.clone.repository.order;

import com.shopee.clone.entity.SellerEntity;
import com.shopee.clone.entity.order.OrderDetailEntity;
import com.shopee.clone.entity.order.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity,Long> {
    List<OrderDetailEntity> findByOrderAndSeller(OrderEntity order, SellerEntity seller);
}
