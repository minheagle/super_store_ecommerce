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
    @GetMapping("order/get-total-on-day")
    public Double getTotalOnDay(){
        return orderService.getTotalOnDay();
    }
    @GetMapping("order/get-total-on-month")
    public Double getTotalOnMonth(@RequestParam(defaultValue = "2023") int year,
                                  @RequestParam(defaultValue = "10") int month){
        return orderService.getTotalOnMonth(year,month);
    }
    @GetMapping("order/get-total-by-seller-on-month/{sellerId}")
    public Double getTotalBySellerOnMonth(@PathVariable Long sellerId,
                                          @RequestParam(defaultValue = "2023") int year,
                                          @RequestParam(defaultValue = "10") int month){
        return orderService.getTotalWithSellerOnMonth(sellerId,year,month);
    }
    @GetMapping("order/get-total-by-seller-on-day/{sellerId}")
    public Double getTotalBySellerOnMonth(@PathVariable Long sellerId){
        return orderService.getTotalWithSellerOnDay(sellerId);
    }
}
