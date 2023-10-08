package com.shopee.clone.service.payment;

import com.shopee.clone.DTO.payment.requestAPI.SignatureRequest;
import com.shopee.clone.DTO.payment.requestServe.PaymentServiceRequest;
import org.springframework.http.ResponseEntity;

public interface IPaymentService {
    ResponseEntity<?> getLinkPayment(PaymentServiceRequest paymentServiceRequest);
    String createSignature(SignatureRequest signatureRequest);
    ResponseEntity<?> getDataPaymentSuccess(Integer code,
                                            String id,
                                            Boolean cancel,
                                            String status,
                                            Integer orderCode);
}
