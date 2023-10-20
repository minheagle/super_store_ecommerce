package com.shopee.clone.rest_controller.order;

import com.shopee.clone.DTO.order.request.OrderRequest;
import com.shopee.clone.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
public class OrderRestController {
    @Autowired
    private OrderService orderService;

    @PostMapping("save-order")
    public ResponseEntity<?> saveOrder(@RequestBody OrderRequest orderRequest){
        return orderService.save(orderRequest);
    }
    @GetMapping("history/{userId}")
    public ResponseEntity<?> getOrderHistory(@PathVariable Long userId){
        return orderService.getHistoryOrder(userId);
    }
    @GetMapping("order/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable Long orderId){
        return orderService.getOrder(orderId);
    }
    @PostMapping("order/cancel/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId){
        return orderService.cancelOrder(orderId);
    }
    @GetMapping("order/top-selling-product")
    public List<Long> getTopSellingProduct(){
        return orderService.getTopSellingProduct();
    }

    @GetMapping("order/top-user")
    public List<Long> findTopUsersByOrderCountInCurrentMonth(){
            return orderService.findTopUsersByOrderCountInCurrentMonth();
    }
    @PostMapping("order/change-status")
    public void changeStatus(@RequestParam Integer orderNumber, @RequestParam Boolean paymentStatus){
        orderService.changeStatusWhenCallPayment(orderNumber, paymentStatus);
    }
    @GetMapping("order/get-total/{orderNumber}")
    public Double getTotalOrderNumber(@PathVariable Integer orderNumber){
        return orderService.getTotalByOrderNumber(orderNumber);
    }

}