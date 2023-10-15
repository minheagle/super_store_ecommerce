package com.shopee.clone.rest_controller.admin;

import com.shopee.clone.service.order.OrderService;
import com.shopee.clone.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @GetMapping("/ban-user/{id}")
    public ResponseEntity<?> banUser(@PathVariable Long id){
        return userService.blockUser(id);
    }

    @GetMapping("/unban-user/{id}")
    public ResponseEntity<?> unBanUser(@PathVariable Long id){
        return userService.unBlockUser(id);
    }
    @GetMapping("/order/get-order-by-seller/{sellerId}")
    public ResponseEntity<?> getAllOrderWithShopOnDay(@PathVariable Long sellerId){
        return orderService.getAllOrderWithShopOnDay(sellerId);
    }
}
