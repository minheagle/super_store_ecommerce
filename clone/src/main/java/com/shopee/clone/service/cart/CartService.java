package com.shopee.clone.service.cart;

import com.shopee.clone.DTO.cart.AddToCartRequest;
import com.shopee.clone.DTO.order.request.CheckOutRequest;
import org.springframework.http.ResponseEntity;

public interface CartService {
    ResponseEntity<?> addToCart(AddToCartRequest addToCartRequest, Long uId);
    ResponseEntity<?> increaseQty(Long pItemId);

    ResponseEntity<?> reduceQty(Long cartId);

    ResponseEntity<?> delete(Long cartId);

    ResponseEntity<?> getCart(Long userId);

    ResponseEntity<?> checkOut(CheckOutRequest listCartId);

//    ResponseEntity<?> checkAddress(String address);

    ResponseEntity<?> updateQty(Long cartId, Integer qty);
}
