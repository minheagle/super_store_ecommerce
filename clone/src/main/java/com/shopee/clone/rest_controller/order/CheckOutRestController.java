package com.shopee.clone.rest_controller.order;

import com.shopee.clone.DTO.order.request.CheckOutRequest;
import com.shopee.clone.service.cart.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
public class CheckOutRestController {
    @Autowired
    private CartService cartService;
    @PostMapping("check-out")
    public ResponseEntity<?> checkOut(@RequestBody CheckOutRequest listCartId){
        return cartService.checkOut(listCartId);
    }
//    @PostMapping("check-address")
//    public ResponseEntity<?> checkAddress(@RequestBody String address){
//        return cartService.checkAddress(address);
//    }
}
