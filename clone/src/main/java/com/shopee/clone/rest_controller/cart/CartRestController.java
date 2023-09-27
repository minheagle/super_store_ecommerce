package com.shopee.clone.rest_controller.cart;

import com.shopee.clone.service.cart.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
public class CartRestController {
    @Autowired
    private CartService cartService;
    @PostMapping("add-to-cart/{pItemId}/{uId}")
    public ResponseEntity<?> addToCart(@PathVariable Long pItemId,@PathVariable Long uId){
        return cartService.addToCart(pItemId,uId);
    }

    @PostMapping("update-qty-cart/up/{cartId}")
    public ResponseEntity<?> increaseQty(@PathVariable Long cartId){
        return cartService.increaseQty(cartId);
    }

    @PostMapping("update-qty-cart/down/{cartId}")
    public ResponseEntity<?> reduceQty(@PathVariable Long cartId){
        return cartService.reduceQty(cartId);
    }
    @PostMapping("delete/{cartId}")
    public ResponseEntity<?> delete(@PathVariable Long cartId){
        return cartService.delete(cartId);
    }

    @GetMapping("get/{userId}")
    public ResponseEntity<?> getCart(@PathVariable Long userId){
        return cartService.getCart(userId);
    }
}
