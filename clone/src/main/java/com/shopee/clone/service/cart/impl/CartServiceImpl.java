package com.shopee.clone.service.cart.impl;

import com.shopee.clone.DTO.cart.CartResponse;
import com.shopee.clone.DTO.product.response.OptionTypeDTO;
import com.shopee.clone.DTO.product.response.OptionValueDTO;
import com.shopee.clone.DTO.product.response.ProductItemResponseDTO;
import com.shopee.clone.DTO.product.response.ProductMatchToCartResponse;
import com.shopee.clone.entity.ProductItemEntity;
import com.shopee.clone.entity.UserEntity;
import com.shopee.clone.entity.cart.CartEntity;
import com.shopee.clone.repository.cart.CartRepository;
import com.shopee.clone.repository.product.OptionTypeRepository;
import com.shopee.clone.repository.product.ProductItemRepository;
import com.shopee.clone.service.cart.CartService;
import com.shopee.clone.service.productItem.impl.ProductItemService;
import com.shopee.clone.service.user.UserService;
import com.shopee.clone.util.ResponseObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    private ProductItemRepository productItemRepository;
    @Autowired
    private ProductItemService productItemService;
    @Override
    public ResponseEntity<?> addToCart(Long pItemId, Long uId) {
        try{
            CartEntity cartEntity = new CartEntity();
            Optional<UserEntity> user = userService.findUserByID(uId);
            if (user.isPresent()) {
                List<CartEntity> cartEntities = cartRepository.findByUser(user.get());
                Optional<ProductItemEntity> productItem = productItemRepository.findById(pItemId);
                if (productItem.isPresent()) {
                    Long check = findCartItemId(cartEntities,productItem.get());
                    if(check!=null) {
                        increaseQty(check);
                        cartEntities = cartRepository.findByUser(user.get());
                        List<CartResponse> cartRepositories = converterCartResponses(cartEntities);
                        return ResponseEntity.ok().body(ResponseObject
                                .builder()
                                .status("SUCCESS")
                                .message("Add to cart success !")
                                .results(cartRepositories)
                                .build());
                    }else {
                        cartEntity.setUser(user.get());
                        cartEntity.setProductItems(productItem.get());
                        cartEntity.setQuantity(1);
                        cartRepository.save(cartEntity);

                        cartEntities = cartRepository.findByUser(user.get());

                        List<CartResponse> cartRepositories = converterCartResponses(cartEntities);
                        return ResponseEntity.ok().body(ResponseObject
                                .builder()
                                .status("SUCCESS")
                                .message("Add to cart success !")
                                .results(cartRepositories)
                                .build());


                    }
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

    private Long findCartItemId(List<CartEntity> cartEntities, ProductItemEntity productItemEntity) {
        Long result = null;
        for (CartEntity cartEntity : cartEntities) {
            if (Objects.equals(cartEntity.getProductItems().getPItemId(), productItemEntity.getPItemId())) {
                result = cartEntity.getId();
                break; // Exit the loop as soon as a match is found
            }
        }
        return result;
    }

    private List<CartResponse> converterCartResponses(List<CartEntity> cartEntities) {
        List<CartResponse> cartResponse =
                cartEntities
                        .stream()
                        .map(c -> {
                            CartResponse cart=  new CartResponse(
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

                            cart.getProduct().setProductItemResponse(productItemDTO);
                            cart.getProduct().getProductItemResponse().setOptionTypes(typeList);

                            return cart;
                        }).toList();
        return cartResponse;
    }

    @Override
    public void remove(Long id) {
    }

    @Override
    public ResponseEntity<?> increaseQty(Long cartId) {
        try{
            Optional<CartEntity> cartOptional = cartRepository.findById(cartId);
            if(cartOptional.isPresent()){
                CartEntity cart = cartOptional.get();
                boolean check = productItemService.checkAvailableQuantityInStock
                        (cart.getProductItems().getPItemId(),cart.getQuantity());
                if(check){
                    cart.setQuantity(cart.getQuantity()+1);
                    cartRepository.save(cart);

                    List<CartEntity> cartList = cartRepository.findByUser(cart.getUser());

                    List<CartResponse> cartRepositories = converterCartResponses(cartList);
                    return ResponseEntity.ok().body(ResponseObject
                            .builder()
                            .status("SUCCESS")
                            .message("Update quantity success !")
                            .results(cartRepositories)
                            .build());

                }
                return ResponseEntity
                        .badRequest()
                        .body(ResponseObject.builder()
                                .status("FAIL")
                                .message("Invalid quantity!")
                                .results("")
                                .build()
                        );

            }
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message("CartId not found!")
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
    public ResponseEntity<?> reduceQty(Long cartId) {
        try{
            Optional<CartEntity> cartOptional = cartRepository.findById(cartId);
            if(cartOptional.isPresent()){
                CartEntity cart = cartOptional.get();
                boolean check = cart.getQuantity()>=1;
                if(check){
                    cart.setQuantity(cart.getQuantity()-1);
                    cartRepository.save(cart);

                    List<CartEntity> cartList = cartRepository.findByUser(cart.getUser());

                    List<CartResponse> cartRepositories = converterCartResponses(cartList);
                    return ResponseEntity.ok().body(ResponseObject
                            .builder()
                            .status("SUCCESS")
                            .message("Update quantity success !")
                            .results(cartRepositories)
                            .build());

                }
                return ResponseEntity
                        .badRequest()
                        .body(ResponseObject.builder()
                                .status("FAIL")
                                .message("Invalid quantity!")
                                .results("")
                                .build()
                        );

            }
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message("CartId not found!")
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
    public ResponseEntity<?> delete(Long cartId) {
        try{
            Optional<CartEntity> cartOptional = cartRepository.findById(cartId);
            if(cartOptional.isPresent()){
                CartEntity cart = cartOptional.get();

                    cartRepository.delete(cart);

                    List<CartEntity> cartList = cartRepository.findByUser(cart.getUser());

                    List<CartResponse> cartRepositories = converterCartResponses(cartList);
                    return ResponseEntity.ok().body(ResponseObject
                            .builder()
                            .status("SUCCESS")
                            .message("Delete product item in cart success !")
                            .results(cartRepositories)
                            .build());
            }
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message("CartId not found!")
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
    public ResponseEntity<?> getCart(Long userId) {
        try{
            Optional<UserEntity> userOptional = userService.findUserByID(userId);
            if(userOptional.isPresent()) {
                UserEntity user = userOptional.get();
                List<CartEntity> cartList = cartRepository.findByUser(user);
                List<CartResponse> cartRepositories = converterCartResponses(cartList);
                return ResponseEntity.ok().body(ResponseObject
                        .builder()
                        .status("SUCCESS")
                        .message("View cart!")
                        .results(cartRepositories)
                        .build());
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
}
