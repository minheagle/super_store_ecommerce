package com.shopee.clone.service.order;

import com.shopee.clone.DTO.order.request.OrderRequest;
import com.shopee.clone.DTO.product.response.ProductResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService{

    ResponseEntity<?> save(OrderRequest orderRequest);
    void changeStatusWhenCallPayment(Integer orderNumber, Boolean bl);

    ResponseEntity<?> getHistoryOrder(Long userId);

    ResponseEntity<?> getOrder(Long orderId);

    ResponseEntity<?> cancelOrder(Long orderId);

    ResponseEntity<?> confirmOrder(Long sellerId, Long orderId);

    ResponseEntity<?> getOrderBySeller(Long sellerId);

    ResponseEntity<?> rejectionOrder(Long sellerId, Long orderId);

    ResponseEntity<?> getAllOrderConfirm();
    int randomOrder();
    ResponseEntity<?> callApi();
    List<Long> getTopSellingProduct();

    List<Long> findTopUsersByOrderCountInCurrentMonth();
}
