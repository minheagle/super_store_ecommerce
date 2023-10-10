package com.shopee.clone.repository.order;

import com.shopee.clone.entity.SellerEntity;
import com.shopee.clone.entity.UserEntity;
import com.shopee.clone.entity.order.EOrder;
import com.shopee.clone.entity.order.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity,Long> {
    List<OrderEntity> findAllByUser(UserEntity userEntity);

    List<OrderEntity> findAllBySeller(SellerEntity seller);

    List<OrderEntity> findByConfirmDateBetweenAndStatus(Date startOfYesterday, Date endOfYesterday, EOrder eOrder);

    Optional<OrderEntity> findByOrderNumber(int orderNumber);
    @Query("SELECT o.user.id " +
            "FROM OrderEntity o " +
            "WHERE MONTH(o.date) = :month " +
            "GROUP BY o.user.id " +
            "ORDER BY COUNT(o.id) DESC")
    List<Long> findTopUserIdsByOrderCountInMonth(int month);

    List<OrderEntity> findAllByOrderNumber(Integer orderNumber);
}
