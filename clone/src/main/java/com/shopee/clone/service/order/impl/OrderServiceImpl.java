package com.shopee.clone.service.order.impl;

import com.shopee.clone.DTO.ResponseData;
import com.shopee.clone.DTO.order.request.OrderRequest;
import com.shopee.clone.DTO.order.response.OrderDetailResponse;
import com.shopee.clone.DTO.order.response.OrderResponse;
import com.shopee.clone.DTO.product.response.OptionTypeDTO;
import com.shopee.clone.DTO.product.response.OptionValueDTO;
import com.shopee.clone.DTO.product.response.ProductItemResponseDTO;
import com.shopee.clone.DTO.product.response.ProductMatchToCartResponse;
import com.shopee.clone.DTO.seller.response.Seller;
import com.shopee.clone.entity.AddressEntity;
import com.shopee.clone.entity.SellerEntity;
import com.shopee.clone.entity.UserEntity;
import com.shopee.clone.entity.cart.CartEntity;
import com.shopee.clone.entity.order.*;
import com.shopee.clone.repository.AddressRepository;
import com.shopee.clone.repository.SellerRepository;
import com.shopee.clone.repository.cart.CartRepository;
import com.shopee.clone.repository.order.OrderDetailRepository;
import com.shopee.clone.repository.order.OrderRepository;
import com.shopee.clone.service.order.OrderService;
import com.shopee.clone.service.productItem.impl.ProductItemService;
import com.shopee.clone.service.user.UserService;
import com.shopee.clone.util.ResponseObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private  CartRepository cartRepository;
    @Autowired
    private  OrderDetailRepository orderDetailRepository;
    @Autowired
    private  SellerRepository sellerRepository;
    @Autowired
    private  AddressRepository addressRepository;
    @Autowired
    private  ModelMapper mapper;
    @Autowired
    private  ProductItemService productItemService;
    @Autowired
    private  UserService userService;

    @Transactional
    @Override
    public ResponseEntity<?> save(OrderRequest orderRequest) {
        try {
            List<OrderResponse> list = new ArrayList<>();
            Optional<UserEntity> userOptional = userService.findUserByID(orderRequest.getUserId());
            Optional<AddressEntity> addressOptional = addressRepository.findById(orderRequest.getAddressId());
            if(userOptional.isPresent() && addressOptional.isPresent()){

//              Chạy vòng lặp để lưu các đơn hàng theo từng shop
                orderRequest.getListOrderBelongToSeller().forEach(o ->{

                    UserEntity user = userOptional.get();
                    AddressEntity address = addressOptional.get();
                    OrderEntity orderEntity = new OrderEntity();

                    orderEntity.setUser(user);
                    orderEntity.setAddress(address);
                    orderEntity.setDate(Date.from(Instant.now()));
                    orderEntity.setNoteTimeRecipient(orderRequest.getNoteTimeRecipient());
                    orderEntity.setPayment(orderRequest.getPaymentMethod());

//                  True là thanh toán rồi
                    if(orderEntity.getPayment()){
                        orderEntity.setStatus(EOrder.Transferred);
                    }else orderEntity.setStatus(EOrder.Pending);

                    Optional<SellerEntity> sellerOptional = sellerRepository.findById(o.getSellerId());
                    if(sellerOptional.isPresent()){
                        SellerEntity seller = sellerOptional.get();
                        orderEntity.setSeller(seller);

                    OrderEntity order =  orderRepository.save(orderEntity);
                    List<CartEntity> cartList = cartRepository.findAllById(o.getCartId());
//                  Chuyển giỏ hàng thành chi tiết đơn hàng
                    List<OrderDetailEntity> orderDetailEntityList =
                            cartList.stream().map(c-> {
                                OrderDetailEntity orderDetail = new OrderDetailEntity();
                                orderDetail.setProductItems(c.getProductItems());
//                              giảm số lượng sản phẩm tồn kho
                                productItemService.minusQuantityInStock(
                                        orderDetail.getProductItems().getPItemId(),
                                        c.getQuantity());
                                orderDetail.setQuantity(c.getQuantity());
                                orderDetail.setUnitPrice(c.getProductItems().getPrice());
                                orderDetail.setOrder(order);
                                return orderDetail;
                            }).toList();

//                  Xóa tất cả giỏ hàng đã mua
                    cartRepository.deleteAll(cartList);
                    List<OrderDetailEntity> orderDetails =
                            orderDetailRepository.saveAll(orderDetailEntityList);
                    order.setOrderDetails(orderDetails);
                    OrderResponse response = convertOrderEntityToOrderResponse(order);
                    list.add(response);
                    }

                });


                ResponseData<Object> data = ResponseData.builder().data(list).build();
                return ResponseEntity
                        .status(HttpStatusCode.valueOf(200))
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("Save order Success")
                                        .results(data)
                                        .build()
                        );

            }
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message("Tham so khong ton tai!")
                            .results("")
                            .results("")
                            .build()
                    );

            }
        catch (Exception e){
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
    public ResponseEntity<?> getHistoryOrder(Long userId) {
        try {
            Optional<UserEntity> userOptional = userService.findUserByID(userId);
            if(userOptional.isPresent()){
                UserEntity user = userOptional.get();
                List<OrderEntity> orderList = orderRepository.findAllByUser(user);
//              Trả về Json
                List<OrderResponse> responses = new ArrayList<>();

                orderList.forEach(order->{
                    OrderResponse orderResponse = convertOrderEntityToOrderResponse(order);
                    responses.add(orderResponse);
                });

                ResponseData<Object> data = ResponseData.builder().data(responses).build();

                return ResponseEntity.ok().body(ResponseObject
                        .builder()
                        .status("SUCCESS")
                        .message("Show history Order success!")
                        .results(data)
                        .build());
            }



        }
        catch (Exception e){
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message(e.getMessage())
                            .results("")
                            .build()
                    );
        }
        return null;
    }

    private OrderResponse convertOrderEntityToOrderResponse(OrderEntity order) {
//              Tạo từng đơn hàng theo Json
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(order.getId());

        orderResponse.setSeller(mapper.map(order.getSeller(),Seller.class));
        orderResponse.setPayment(order.getPayment());
        orderResponse.setDate(order.getDate());
        orderResponse.setShipMoney(order.getShipMoney());
        orderResponse.setStatus(order.getStatus().name());

        List<OrderDetailResponse> orderDetailResponseList =
        order.getOrderDetails().stream().map(this::convertOrderDetailToODResponse).toList();
        orderResponse.setOrderDetailList(orderDetailResponseList);
        return orderResponse;
    }

    private OrderDetailResponse convertOrderDetailToODResponse(OrderDetailEntity x) {
        OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
        orderDetailResponse.setId(x.getId());
        orderDetailResponse.setQuantity(x.getQuantity());
        orderDetailResponse.setUnitPrice(x.getUnitPrice());
        orderDetailResponse.setProduct(mapper.map(x.getProductItems().getProduct(), ProductMatchToCartResponse.class));

        ProductItemResponseDTO productItemDTO = mapper.map(x.getProductItems(), ProductItemResponseDTO.class);

        List<OptionTypeDTO> typeList = new ArrayList<>();
        x.getProductItems()
                .getOptionValues()
                .forEach(v ->
                {
                    OptionTypeDTO type = mapper.map(v.getOptionType(), OptionTypeDTO.class);
                    type.setOptionValue(mapper.map(v, OptionValueDTO.class));
                    typeList.add(type);
                });

        orderDetailResponse.getProduct().setProductItemResponse(productItemDTO);
        orderDetailResponse.getProduct().getProductItemResponse().setOptionTypes(typeList);
        return orderDetailResponse;
    }

    @Override
    public ResponseEntity<?> getOrder(Long orderId) {
        try {
            Optional<OrderEntity> orderEntity = orderRepository.findById(orderId);
            if(orderEntity.isPresent()){
                OrderEntity order = orderEntity.get();
//              Trả về Json
                OrderResponse orderResponse = convertOrderEntityToOrderResponse(order);

                ResponseData<Object> data = ResponseData.builder().data(orderResponse).build();

                return ResponseEntity.ok().body(ResponseObject
                        .builder()
                        .status("SUCCESS")
                        .message("Get Order success!")
                        .results(data)
                        .build());
            }
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message("Order not exist!")
                            .results("")
                            .build()
                    );

        }
        catch (Exception e){
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
    public ResponseEntity<?> cancelOrder(Long orderId) {
        try {
            Optional<OrderEntity> orderEntity = orderRepository.findById(orderId);
            if(orderEntity.isPresent()){
                OrderEntity order = orderEntity.get();

                order.setStatus(EOrder.Cancelled);

//                Trả lại số lượng cho order

                orderRepository.save(order);
//              Trả về Json
                OrderResponse orderResponse = convertOrderEntityToOrderResponse(order);

                ResponseData<Object> data = ResponseData.builder().data(orderResponse).build();

                return ResponseEntity.ok().body(ResponseObject
                        .builder()
                        .status("SUCCESS")
                        .message("Cancel Order success!")
                        .results(data)
                        .build());
            }
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message("Order not exist!")
                            .results("")
                            .build()
                    );

        }
        catch (Exception e){
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
