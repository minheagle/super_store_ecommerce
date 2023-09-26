package com.shopee.clone.service.order.impl;

import com.shopee.clone.DTO.order.request.OrderRequest;
import com.shopee.clone.entity.ProductItemEntity;
import com.shopee.clone.entity.SellerEntity;
import com.shopee.clone.entity.UserEntity;
import com.shopee.clone.entity.cart.CartEntity;
import com.shopee.clone.entity.order.EOrder;
import com.shopee.clone.entity.order.EPayment;
import com.shopee.clone.entity.order.OrderDetailEntity;
import com.shopee.clone.entity.order.OrderEntity;
import com.shopee.clone.repository.SellerRepository;
import com.shopee.clone.repository.cart.CartRepository;
import com.shopee.clone.repository.order.OrderDetailRepository;
import com.shopee.clone.repository.order.OrderRepository;
import com.shopee.clone.repository.product.ProductItemRepository;
import com.shopee.clone.service.order.OrderService;
import com.shopee.clone.service.user.UserService;
import com.shopee.clone.util.ResponseObject;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
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

}
