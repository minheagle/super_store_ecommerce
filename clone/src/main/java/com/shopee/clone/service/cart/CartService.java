package com.shopee.clone.service.cart;

import org.springframework.http.ResponseEntity;

public interface CartService {
    ResponseEntity<?> addToCart(Long pItemId, Long uId);
    void remove(Long id);
}
