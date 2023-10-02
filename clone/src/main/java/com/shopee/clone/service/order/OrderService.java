package com.shopee.clone.service.order;

import com.shopee.clone.DTO.order.request.OrderRequest;
import org.springframework.http.ResponseEntity;

public interface OrderService{

    ResponseEntity<?> save(OrderRequest orderRequest);

    ResponseEntity<?> getHistoryOrder(Long userId);

    ResponseEntity<?> getOrder(Long orderId);

    ResponseEntity<?> cancelOrder(Long orderId);
}
