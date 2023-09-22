package com.shopee.clone.service.cart.impl;

import com.shopee.clone.DTO.auth.refresh_token.RefreshTokenResponse;
import com.shopee.clone.DTO.cart.CartResponse;
import com.shopee.clone.DTO.product.OptionType;
import com.shopee.clone.DTO.product.OptionValue;
import com.shopee.clone.DTO.product.ProductItem;
import com.shopee.clone.DTO.product.response.*;
import com.shopee.clone.entity.*;
import com.shopee.clone.entity.cart.CartEntity;
import com.shopee.clone.repository.cart.CartRepository;
import com.shopee.clone.repository.product.OptionTypeRepository;
import com.shopee.clone.repository.product.OptionValueRepository;
import com.shopee.clone.repository.product.ProductItemRepository;
import com.shopee.clone.service.cart.CartService;
import com.shopee.clone.service.user.UserService;
import com.shopee.clone.util.ResponseObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service

public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private OptionTypeRepository optionType;
    @Autowired
    private OptionValueRepository optionValue;
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



                    List<CartResponse> cartRepositories =
                            cartEntities
                                    .stream()
                                    .map(c -> {
                                        CartResponse cartResponse=  new CartResponse(
                                            modelMapper
                                                    .map(c.getProductItems().getProduct(), ProductMatchToCartResponse.class)
                                                     ,c.getQuantity());
                                        ProductItemResponseDTO productItemDTO = modelMapper.map(c.getProductItems(), ProductItemResponseDTO.class);

                                        List<OptionTypeDTO> typeList = new ArrayList<>();
                                        c.getProductItems()
                                                .getOptionValues()
                                                .forEach(v->
                                                {
                                                    OptionTypeDTO type = modelMapper.map(v.getOptionType(),OptionTypeDTO.class);
                                                    type.setOptionValue(modelMapper.map(v,OptionValueDTO.class));
                                                    typeList.add(type);
                                                });

                                        cartResponse.getProduct().setProductItemResponse(productItemDTO);
                                        cartResponse.getProduct().getProductItemResponse().setOptionTypes(typeList);

                                        return cartResponse;
                                    }).toList();
                    return ResponseEntity.ok().body(ResponseObject
                            .builder()
                            .status("SUCCESS")
                            .message("Add to cart success !")
                            .results(cartRepositories)
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
