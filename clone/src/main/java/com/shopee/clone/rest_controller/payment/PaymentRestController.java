package com.shopee.clone.rest_controller.payment;

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

    @GetMapping("success")
    public ResponseEntity<?> getDataPaymentSuccess(@RequestParam(name = "code", required = false) Integer code,
                                                    @RequestParam(name = "id", required = false) String id,
                                                    @RequestParam(name = "cancel", required = false) Boolean cancel,
                                                    @RequestParam(name = "status", required = false) String status,
                                                    @RequestParam(name = "orderCode", required = false) Integer orderCode){

        return paymentService.getDataPaymentSuccess(code, id, cancel, status, orderCode);
    }

    @GetMapping("cancel")
    public ResponseEntity<?> getDataPaymentCancel(@RequestParam(name = "code", required = false) Integer code,
                                                   @RequestParam(name = "id", required = false) String id,
                                                   @RequestParam(name = "cancel", required = false) Boolean cancel,
                                                   @RequestParam(name = "status", required = false) String status,
                                                   @RequestParam(name = "orderCode", required = false) Integer orderCode){

        return paymentService.getDataPaymentSuccess(code, id, cancel, status, orderCode);
    }

    @PostMapping("")
    public ResponseEntity<?> getLinkPayment(@RequestBody PaymentServiceRequest paymentServiceRequest){

        return paymentService.getLinkPayment(paymentServiceRequest);
    }

}
