package com.shopee.clone.service.cart.impl;

import com.shopee.clone.DTO.auth.refresh_token.RefreshTokenResponse;
import com.shopee.clone.entity.ProductItemEntity;
import com.shopee.clone.entity.RefreshTokenEntity;
import com.shopee.clone.entity.UserEntity;
import com.shopee.clone.entity.cart.CartEntity;
import com.shopee.clone.repository.cart.CartRepository;
import com.shopee.clone.repository.product.ProductItemRepository;
import com.shopee.clone.service.cart.CartService;
import com.shopee.clone.service.user.UserService;
import com.shopee.clone.util.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service

public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductItemRepository productItemRepository;
    @Override
    public ResponseEntity<?> addToCart(Long pItemId, Long uId) {
        try{
            CartEntity cartEntity = new CartEntity();
            Optional<UserEntity> user = userService.findUserByID(uId);
            if (user.isPresent()) {
                Optional<ProductItemEntity> productItem = productItemRepository.findById(pItemId);
                if (productItem.isPresent()) {
                    cartEntity.setUser(user.get());
                    cartEntity.setProductItems(productItem.get());
                    cartEntity.setQuantity(1);
                    cartRepository.save(cartEntity);

                    List<CartEntity> cartEntities = cartRepository.findByUser(user.get());

                    return ResponseEntity.ok().body(ResponseObject
                            .builder()
                            .status("SUCCESS")
                            .message("Add to cart success !")
                            .results(cartEntities)
                            .build());
                }
                return ResponseEntity
                        .badRequest()
                        .body(ResponseObject.builder()
                                .status("FAIL")
                                .message("ProductItem not found!")
                                .results("")
                                .build()
                        );
            }
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message("User not found!")
                            .results("")
                            .build()
                    );
        }catch (Exception e){
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message(e.getMessage())
                            .results("")
                            .build()
                    );
        }

    }

    @Override
    public void remove(Long id) {
        Optional<CartEntity> cartEntity = cartRepository.findById(id);
        cartRepository.delete(cartEntity.get());
    }
}
