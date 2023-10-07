package com.shopee.clone.rest_controller.cart;

import com.shopee.clone.DTO.cart.AddToCartRequest;
import com.shopee.clone.service.cart.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@PreAuthorize("hasRole('ROLE_USER')")
public class CartRestController {
    @Autowired
    private CartService cartService;
    @PostMapping("add-to-cart/{uId}")
    public ResponseEntity<?> addToCart(@PathVariable Long uId, @RequestBody AddToCartRequest addToCartRequest){
        return cartService.addToCart(addToCartRequest, uId);
    }

    @PostMapping("update/{cartId}/{qty}")
    public ResponseEntity<?> updateQty(@PathVariable Long cartId, @PathVariable Integer qty){
        return cartService.updateQty(cartId,qty);
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
