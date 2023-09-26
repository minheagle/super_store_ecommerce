package com.shopee.clone.service.order;

import com.shopee.clone.DTO.order.request.OrderRequest;
import com.shopee.clone.repository.order.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

public interface OrderService{

    ResponseEntity<?> save(OrderRequest orderRequest);

    ResponseEntity<?> getHistoryOrder(Long userId);
}
