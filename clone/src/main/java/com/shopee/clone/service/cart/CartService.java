package com.shopee.clone.service.cart;

import com.shopee.clone.DTO.order.request.CheckOutRequest;
import com.shopee.clone.DTO.order.request.OrderInformationRequest;
import org.springframework.http.ResponseEntity;

public interface CartService {
    ResponseEntity<?> addToCart(Long pItemId, Long uId);
    ResponseEntity<?> increaseQty(Long pItemId);

    ResponseEntity<?> reduceQty(Long cartId);

    ResponseEntity<?> delete(Long cartId);

    ResponseEntity<?> getCart(Long userId);

    ResponseEntity<?> checkOut(CheckOutRequest listCartId);

    ResponseEntity<?> checkAddress(CheckOutRequest order);
}
