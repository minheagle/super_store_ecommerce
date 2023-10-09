package com.shopee.clone.repository.payment;

import com.shopee.clone.entity.payment.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    PaymentEntity findByOrderNumber(Integer orderNumber);
}
