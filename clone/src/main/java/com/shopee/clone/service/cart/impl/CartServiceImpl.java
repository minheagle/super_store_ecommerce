package com.shopee.clone.service.cart.impl;

import com.shopee.clone.DTO.ResponseData;
import com.shopee.clone.DTO.call_api_delivery.request.GetMoneyShip;
import com.shopee.clone.DTO.cart.AddToCartRequest;
import com.shopee.clone.DTO.cart.CartResponse;
import com.shopee.clone.DTO.cart.LineItem;
import com.shopee.clone.DTO.cart.ProductItemMatchToCart;
import com.shopee.clone.DTO.checkAddress.AddressRequest;
import com.shopee.clone.DTO.order.request.CheckOutRequest;
import com.shopee.clone.DTO.order.response.CheckOutResponse;
import com.shopee.clone.DTO.product.response.OptionTypeDTO;
import com.shopee.clone.DTO.product.response.OptionValueDTO;
import com.shopee.clone.DTO.product.response.ProductItemResponseDTO;
import com.shopee.clone.DTO.product.response.ProductMatchToCartResponse;
import com.shopee.clone.DTO.seller.response.Seller;
import com.shopee.clone.entity.ProductItemEntity;
import com.shopee.clone.entity.UserEntity;
import com.shopee.clone.entity.cart.CartEntity;
import com.shopee.clone.repository.cart.CartRepository;
import com.shopee.clone.repository.product.ProductItemRepository;
import com.shopee.clone.service.cart.CartService;
import com.shopee.clone.service.productItem.impl.ProductItemService;
import com.shopee.clone.service.user.UserService;
import com.shopee.clone.util.ResponseObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    private static final String DELIVERY_API_URL = "DELIVERY_API_URL";
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ProductItemRepository productItemRepository;
    @Autowired
    private ProductItemService productItemService;

    @Override
    public ResponseEntity<?> addToCart(AddToCartRequest addToCartRequest, Long uId) {
        try{
            CartEntity cartEntity = new CartEntity();
            Optional<UserEntity> user = userService.findUserByID(uId);
            if (user.isPresent()) {
                List<CartEntity> cartEntities = cartRepository.findByUser(user.get());
                Optional<ProductItemEntity> productItem = productItemRepository.findById(addToCartRequest.getProductItemId());
                if (productItem.isPresent()) {
                    Long check = findCartItemId(cartEntities, productItem.get());
                    if(check!=null) {
//                        increaseQty(check);
                        updateQuantity(check,addToCartRequest.getQuantity());
                        cartEntities = cartRepository.findByUser(user.get());
                        List<CartResponse> cartRepositories = convertCartResponses(cartEntities);
                        ResponseData<Object> response = ResponseData.builder().data(cartRepositories).build();
                        return ResponseEntity.ok().body(ResponseObject
                                .builder()
                                .status("SUCCESS")
                                .message("Add to cart success !")
                                .results(response)
                                .build());
                    }else {
                        cartEntity.setUser(user.get());
                        cartEntity.setProductItems(productItem.get());
                        cartEntity.setQuantity(addToCartRequest.getQuantity());
                        cartEntity.setSeller(productItem.get().getProduct().getSeller());
                        cartRepository.save(cartEntity);

                        cartEntities = cartRepository.findByUser(user.get());

                        List<CartResponse> cartRepositories = convertCartResponses(cartEntities);

                        ResponseData<Object> response = ResponseData.builder().data(cartRepositories).build();
                        return ResponseEntity.ok().body(ResponseObject
                                .builder()
                                .status("SUCCESS")
                                .message("Add to cart success !")
                                .results(response)
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

    public void updateQuantity(Long cartId,Integer quantity) {
        try{
            Optional<CartEntity> cartOptional = cartRepository.findById(cartId);
            if(cartOptional.isPresent()){
                CartEntity cart = cartOptional.get();
//                kiểm tra số lượng sản phẩm còn đủ không?
                boolean check = productItemService.checkAvailableQuantityInStock
                        (cart.getProductItems().getPItemId(),cart.getQuantity()+quantity);
                if(check){
                    cart.setQuantity(cart.getQuantity()+quantity);
                    cartRepository.save(cart);
                }
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
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

    private List<CartResponse> convertCartResponses(List<CartEntity> cartEntities){
        List<CartResponse> responses = new ArrayList<>() ;
        List<Long> sellers = new ArrayList<>();
        cartEntities
                .forEach(c -> {
                    if(!sellers.contains(c.getSeller().getId())){
                    sellers.add(c.getSeller().getId());
                    CartResponse cartResponse = new CartResponse();
                    cartResponse.setSeller(modelMapper.map(c.getSeller(), Seller.class));
                    List<CartEntity> cartList = cartRepository.findByUserAndSeller(c.getUser(),c.getSeller());
                    cartResponse.setLineItems(convertLineItem(cartList));
                    responses.add(cartResponse);
                    }
                });
        return responses;
    }
    private List<LineItem> convertLineItem(List<CartEntity> cartEntities) {

        List<LineItem> lineItems;
        lineItems = cartEntities
                .stream()
                .map(c -> {
                    LineItem lineItem=  new LineItem(c.getId(),
                        modelMapper
                                .map(c.getProductItems().getProduct(), ProductMatchToCartResponse.class)
                                 ,c.getQuantity());
                    ProductItemMatchToCart productItemDTO = modelMapper.map(c.getProductItems(), ProductItemMatchToCart.class);

                    List<OptionTypeDTO> typeList = new ArrayList<>();
                    c.getProductItems()
                            .getOptionValues()
                            .forEach(v->
                            {
                                OptionTypeDTO type = modelMapper.map(v.getOptionType(),OptionTypeDTO.class);
                                type.setOptionValue(modelMapper.map(v,OptionValueDTO.class));
                                typeList.add(type);
                            });

                    lineItem.getProduct().setProductItemResponse(productItemDTO);
                    lineItem.getProduct().getProductItemResponse().setOptionTypes(typeList);

                    return lineItem;
                }).toList();
        return lineItems;
    }

    @Override
    public ResponseEntity<?> increaseQty(Long cartId) {
        try{
            Optional<CartEntity> cartOptional = cartRepository.findById(cartId);
            if(cartOptional.isPresent()){
                CartEntity cart = cartOptional.get();
//                kiểm tra số lượng sản phẩm còn đủ không?
                boolean check = productItemService.checkAvailableQuantityInStock
                        (cart.getProductItems().getPItemId(),cart.getQuantity()+1);
                if(check){
                    cart.setQuantity(cart.getQuantity()+1);
                    cartRepository.save(cart);

                    List<CartEntity> cartList = cartRepository.findByUser(cart.getUser());

                    List<CartResponse> cartRepositories = convertCartResponses(cartList);
                    ResponseData<Object> response = ResponseData.builder().data(cartRepositories).build();
                    return ResponseEntity.ok().body(ResponseObject
                            .builder()
                            .status("SUCCESS")
                            .message("Update quantity success!")
                            .results(response)
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

                    List<CartResponse> cartRepositories = convertCartResponses(cartList);
                    ResponseData<Object> response = ResponseData.builder().data(cartRepositories).build();
                    return ResponseEntity.ok().body(ResponseObject
                            .builder()
                            .status("SUCCESS")
                            .message("Update quantity success!")
                            .results(response)
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

                    List<CartResponse> cartRepositories = convertCartResponses(cartList);
                ResponseData<Object> response = ResponseData.builder().data(cartRepositories).build();
                return ResponseEntity.ok().body(ResponseObject
                        .builder()
                        .status("SUCCESS")
                        .message("Delete product in cart success!")
                        .results(response)
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
                List<CartResponse> cartRepositories = convertCartResponses(cartList);
                ResponseData<Object> response = ResponseData.builder().data(cartRepositories).build();
                return ResponseEntity.ok().body(ResponseObject
                        .builder()
                        .status("SUCCESS")
                        .message("Get cart by UserID success !")
                        .results(response)
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

    @Override
    public ResponseEntity<?> checkOut(CheckOutRequest checkOutRequest) {
        try{
                List<CartEntity> cartList = cartRepository.findAllById(checkOutRequest.getListCartId());
                List<CartResponse> cartResponse = convertCartResponses(cartList);
                List<CheckOutResponse> checkOutResponses =
                        cartResponse.stream().map(c -> {
                    CheckOutResponse checkOutResponse = new CheckOutResponse();
                    checkOutResponse.setCartResponse(c);
                    GetMoneyShip getMoneyShip = new GetMoneyShip();
                    getMoneyShip.setAddressOfShop(c.getSeller().getStoreAddress());
                    getMoneyShip.setAddressOfUser(checkOutRequest.getShipAddress());
                    getMoneyShip.setQuantity(c.getLineItems().size());
                    Double shipMoney = 30000D * getMoneyShip.getQuantity();
//                    Double shipMoney =
//                            restTemplate
//                                    .postForObject
//                                            (DELIVERY_API_URL,
//                                                    getMoneyShip
//                                                    , Double.class);
                    checkOutResponse.setShipMoney(shipMoney);
                    return checkOutResponse;
                }).toList();

            ResponseData<Object> response = ResponseData.builder().data(checkOutResponses).build();

                return ResponseEntity.ok().body(ResponseObject
                        .builder()
                        .status("SUCCESS")
                        .message("View cart!")
                        .results(response)
                        .build());
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
    public ResponseEntity<?> checkAddress(CheckOutRequest order) {
        try{
            boolean check =  isAddressValid(order);
            if(check){
            return ResponseEntity.ok().body(ResponseObject
                    .builder()
                    .status("SUCCESS")
                    .message("Valid address!")
                    .results(true)
                    .build());
            }
            return ResponseEntity.ok().body(ResponseObject
                    .builder()
                    .status("Fail")
                    .message("Invalid address!")
                    .results(false)
                    .build());
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

    private boolean isAddressValid(CheckOutRequest order) {
        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setAddress(order.getShipAddress());

        // Gọi API và nhận giá trị boolean trả về
        return Boolean.TRUE.equals(
                restTemplate.postForObject(DELIVERY_API_URL, addressRequest, Boolean.class));
    }
}
