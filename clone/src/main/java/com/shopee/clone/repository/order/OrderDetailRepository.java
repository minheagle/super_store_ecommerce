package com.shopee.clone.repository.order;

import com.shopee.clone.entity.ProductEntity;
import com.shopee.clone.entity.SellerEntity;
import com.shopee.clone.entity.order.OrderDetailEntity;
import com.shopee.clone.entity.order.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity,Long> {
    @Query("SELECT od.productItems.product " +
            "FROM OrderDetailEntity od " +
            "GROUP BY od.productItems.product " +
            "ORDER BY COUNT(od.productItems.product) DESC")
    List<ProductEntity> findMostSoldProduct();


}
