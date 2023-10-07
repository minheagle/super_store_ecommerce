package com.shopee.clone.rest_controller.payment;

import com.shopee.clone.service.payment.IPaymentService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentRestController {
    @Autowired
    private IPaymentService paymentService;

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
    public ResponseEntity<?> getLinkPayment(@RequestBody @NotBlank Long userId,
                                            @RequestBody @NotBlank Integer orderNumber,
                                            @RequestBody @NotBlank Integer amountPayment){

        return paymentService.getLinkPayment(userId, orderNumber, amountPayment);
    }

}
