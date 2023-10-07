package com.shopee.clone.service.payment;

import com.shopee.clone.DTO.payment.requestAPI.SignatureRequest;
import org.springframework.http.ResponseEntity;

public interface IPaymentService {
    ResponseEntity<?> getLinkPayment(Long userId, Integer orderNumber, Integer amountPayment);
    String createSignature(SignatureRequest signatureRequest);
    ResponseEntity<?> getDataPaymentSuccess(Integer code,
                                            String id,
                                            Boolean cancel,
                                            String status,
                                            Integer orderCode);
}
