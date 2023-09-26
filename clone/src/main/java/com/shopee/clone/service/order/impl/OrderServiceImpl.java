package com.shopee.clone.service.order.impl;

import com.shopee.clone.DTO.ResponseData;
import com.shopee.clone.DTO.order.request.OrderRequest;
import com.shopee.clone.DTO.order.response.OrderDetailForShop;
import com.shopee.clone.DTO.order.response.OrderDetailResponse;
import com.shopee.clone.DTO.order.response.OrderResponse;
import com.shopee.clone.DTO.product.response.OptionTypeDTO;
import com.shopee.clone.DTO.product.response.OptionValueDTO;
import com.shopee.clone.DTO.product.response.ProductItemResponseDTO;
import com.shopee.clone.DTO.product.response.ProductMatchToCartResponse;
import com.shopee.clone.DTO.seller.response.Seller;
import com.shopee.clone.entity.AddressEntity;
import com.shopee.clone.entity.ProductItemEntity;
import com.shopee.clone.entity.SellerEntity;
import com.shopee.clone.entity.UserEntity;
import com.shopee.clone.entity.cart.CartEntity;
import com.shopee.clone.entity.order.EOrder;
import com.shopee.clone.entity.order.EPayment;
import com.shopee.clone.entity.order.OrderDetailEntity;
import com.shopee.clone.entity.order.OrderEntity;
import com.shopee.clone.repository.AddressRepository;
import com.shopee.clone.repository.SellerRepository;
import com.shopee.clone.repository.cart.CartRepository;
import com.shopee.clone.repository.order.OrderDetailRepository;
import com.shopee.clone.repository.order.OrderRepository;
import com.shopee.clone.repository.product.ProductItemRepository;
import com.shopee.clone.service.order.OrderService;
import com.shopee.clone.service.user.UserService;
import com.shopee.clone.util.ResponseObject;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
    private CartRepository cartRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private ProductItemRepository productItemRepository;
    @Autowired
    private UserService userService;
    @Transactional
    @Override
    public ResponseEntity<?> save(OrderRequest orderRequest) {
        try {
            Optional<UserEntity> userOptional = userService.findUserByID(orderRequest.getUserId());
            if(userOptional.isPresent()){
                OrderEntity orderEntity = new OrderEntity();
                orderEntity.setUser(userOptional.get());
                orderEntity.setDate(Date.from(Instant.now()));
                AddressEntity address = new AddressEntity();
                address.setAddressName(orderRequest.getAddress());
                address.setUser(userOptional.get());
                orderEntity.setAddress(addressRepository.save(address));
                orderEntity.setPayment(EPayment.valueOf(orderRequest.getPaymentMethod()));
                if(orderEntity.getPayment().equals(EPayment.BANK_PAYMENT)) {
                    orderEntity.setStatus(EOrder.Transferred);
                }else { orderEntity.setStatus(EOrder.Pending);}

                orderRepository.save(orderEntity);

                orderRequest
                        .getCartRequest()
                        .forEach(o->{
                            SellerEntity seller = sellerRepository.findById(o.getSellerId()).get();
                            o.getListCartId().forEach(c->{
                                OrderDetailEntity orderDetail = new OrderDetailEntity();
                                orderDetail.setOrder(orderEntity);
                                orderDetail.setSeller(seller);
                                orderDetail.setQuantity(o.getListCartId().size());
                                orderDetail.setShipMoneyOnProduct(o.getShipMoney()/o.getListCartId().size());
                                Optional<CartEntity> cart = cartRepository.findById(c);
                                ProductItemEntity pItem = cart.get().getProductItems();
                                orderDetail.setProductItems(pItem);
                                orderDetail.setUnitPrice(pItem.getPrice());
                                orderDetail.setOrder(orderEntity);
                                orderDetailRepository.save(orderDetail);
                                cartRepository.delete(cart.get());
                            });
                });
            }
            return ResponseEntity
                    .status(HttpStatusCode.valueOf(200))
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .message("Save order Success")
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
                List<OrderEntity> orderList = orderRepository.findAllByUser(userOptional.get());


//              Trả về Json
                List<OrderResponse> responses = new ArrayList<>();

                orderList.forEach(order->{
                    List<Seller> sellers = new ArrayList<>();
//              Tạo từng đơn hàng theo Json
                OrderResponse orderResponse = new OrderResponse();
                orderResponse.setId(order.getId());
                orderResponse.setPaymentMethod(order.getPayment().name());
                orderResponse.setDate(order.getDate());
                orderResponse.setStatus(order.getStatus().name());

                List<OrderDetailForShop> listOrderDetailForShop = new ArrayList<>();
//              Lấy ra các chi tiét đơn hàng
                    order.getOrderDetails().forEach(oD->{

                        if(!sellers.contains(mapper.map(oD.getSeller(), Seller.class))) {
                            sellers.add(mapper.map(oD.getSeller(), Seller.class));
//                  Lọc các orderDetail theo seller
                    List<OrderDetailEntity> orderOp = orderDetailRepository.findByOrderAndSeller(order, oD.getSeller());
                    OrderDetailForShop orderDetailForShop = new OrderDetailForShop();
                    orderDetailForShop.setSeller(mapper.map(oD.getSeller(), Seller.class));
                    orderDetailForShop.setShipMoney(oD.getShipMoneyOnProduct()*orderOp.size());

//                  Tạo một lits Order Detail Response
                    List<OrderDetailResponse> orderDetailResponses = new ArrayList<>();

//                  Chạy vòng lặp để gán mỗi order detail cho một shop
                    orderOp.forEach(x ->{


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

                            orderDetailResponses.add(orderDetailResponse);

                   });

                    orderDetailForShop.setOrderDetailList(orderDetailResponses);
                    listOrderDetailForShop.add(orderDetailForShop);
                        }
                });


                orderResponse.setOrderDetails(listOrderDetailForShop);
                responses.add(orderResponse);
                });

                ResponseData data = new ResponseData();
                data.setData(responses);

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

}
