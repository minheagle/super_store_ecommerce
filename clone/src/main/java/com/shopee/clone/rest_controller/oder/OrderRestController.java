package com.shopee.clone.rest_controller.oder;

import com.shopee.clone.DTO.order.request.CheckOutRequest;
import com.shopee.clone.DTO.order.request.OrderRequest;
import com.shopee.clone.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
