package com.shopee.clone.rest_controller.payment;

import com.shopee.clone.DTO.payment.requestServe.InforReturnStatusPayment;
import com.shopee.clone.DTO.payment.requestServe.PaymentServiceRequest;
import com.shopee.clone.service.payment.IPaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentRestController {
    private final IPaymentService paymentService;

    public PaymentRestController(IPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PatchMapping("status")
   public ResponseEntity<?> changePaymentStatus(@RequestBody InforReturnStatusPayment informationStatusPayment){
        return paymentService.changePaymentStatus(informationStatusPayment);
   }

    @PostMapping("")
    public ResponseEntity<?> getLinkPayment(@RequestBody PaymentServiceRequest paymentServiceRequest){

        return paymentService.getLinkPayment(paymentServiceRequest);
    }

}
