package com.shopee.clone.rest_controller.cart;

import com.shopee.clone.service.cart.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cart")
public class CartRestController {
    @Autowired
    private CartService cartService;
    @GetMapping("add-to-cart/{pItemId}/{uId}")
    public ResponseEntity<?> addToCart(@PathVariable Long pItemId,@PathVariable Long uId){
        return cartService.addToCart(pItemId,uId);
    }

    @GetMapping("update-qty-cart/up/{cartId}")
    public ResponseEntity<?> increaseQty(@PathVariable Long cartId){
        return cartService.increaseQty(cartId);
    }

    @GetMapping("update-qty-cart/down/{cartId}")
    public ResponseEntity<?> reduceQty(@PathVariable Long cartId){
        return cartService.reduceQty(cartId);
    }
    @GetMapping("delete-cart/{cartId}")
    public ResponseEntity<?> delete(@PathVariable Long cartId){
        return cartService.delete(cartId);
    }
}
