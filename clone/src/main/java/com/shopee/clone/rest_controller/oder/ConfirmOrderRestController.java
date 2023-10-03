package com.shopee.clone.rest_controller.oder;

import com.shopee.clone.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/seller/order")
public class ConfirmOrderRestController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/{sellerId}")
    public ResponseEntity<?> getOrder(@PathVariable Long sellerId){
        return orderService.getOrderBySeller(sellerId);
    }

    @PostMapping("confirm-order/{sellerId}/{orderId}")
    public ResponseEntity<?> confirmOrder(@PathVariable Long sellerId,@PathVariable Long orderId){
        return orderService.confirmOrder(sellerId,orderId);
    }

    @PostMapping("rejection-order/{sellerId}/{orderId}")
    public ResponseEntity<?> rejectionOrder(@PathVariable Long sellerId,@PathVariable Long orderId){
        return orderService.rejectionOrder(sellerId,orderId);
    }

    @GetMapping("/get-all-order-confirm")
    public ResponseEntity<?> getAllOrderConfirm(){
        return orderService.getAllOrderConfirm();
    }
}
