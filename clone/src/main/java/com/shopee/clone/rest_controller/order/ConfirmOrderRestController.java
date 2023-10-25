package com.shopee.clone.rest_controller.order;

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

    @GetMapping("/pending/{sellerId}")
    public ResponseEntity<?> getOrderByPending(@PathVariable Long sellerId){
        return orderService.getOrderBySellerAndStatusPending(sellerId);
    }
    @GetMapping("/processing/{sellerId}")
    public ResponseEntity<?> getOrderByProcessing(@PathVariable Long sellerId){
        return orderService.getOrderBySellerAndStatusProcessing(sellerId);
    }
    @GetMapping("/cancel/{sellerId}")
    public ResponseEntity<?> getOrderByCancel(@PathVariable Long sellerId){
        return orderService.getOrderBySellerAndStatusCancel(sellerId);
    }

    @GetMapping("/get-all-order-confirm")
    public ResponseEntity<?> getAllOrderConfirm(){
        return orderService.getAllOrderConfirm();
    }
    @GetMapping("/test-call-api")
    public ResponseEntity<?> testCallApi(){
        return orderService.callApi();
    }

}
