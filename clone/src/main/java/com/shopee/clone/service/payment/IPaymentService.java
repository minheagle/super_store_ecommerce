package com.shopee.clone.service.payment;

import org.springframework.http.ResponseEntity;

public interface IPaymentService {
    ResponseEntity<?> getLinkPayment(Long userId, Integer orderNumber, Integer amountPayment);
}
