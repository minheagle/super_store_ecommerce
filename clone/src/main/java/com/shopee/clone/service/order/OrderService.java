package com.shopee.clone.service.order;

import com.shopee.clone.DTO.order.request.DeliveryStatusRequest;
import com.shopee.clone.DTO.order.request.OrderRequest;
import com.shopee.clone.DTO.product.response.ProductResponseDTO;
import com.shopee.clone.entity.ProductEntity;
import com.shopee.clone.entity.order.OrderDetailEntity;
import com.shopee.clone.entity.order.OrderEntity;
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

//    lấy danh sách các đơn hàng theo seller trong 1 ngày
    ResponseEntity<?> getAllOrderWithShopOnDay(Long sellerId);
//    Lấy tổng số tiền trong 1 ngày
    Double getTotalOnDay();
//    Lấy tổng số tiền trong một tháng
    Double getTotalOnMonth(int year, int month);

    //    Lấy tổng số tiền trong 1 ngày theo seller
    Double getTotalWithSellerOnDay(Long sellerId);
//    Lấy tổng số tiền trong 1 tháng theo seller
    Double getTotalWithSellerOnMonth(Long sellerId, int year, int month);
    List<Long> findTopUsersByOrderCountInCurrentMonth();

    ResponseEntity<?> getOrderBySellerAndStatusPending(Long sellerId);

    ResponseEntity<?> getOrderBySellerAndStatusProcessing(Long sellerId);

    ResponseEntity<?> getOrderBySellerAndStatusCancel(Long sellerId);
    void changeStatusWhenDelivery(DeliveryStatusRequest deliveryStatusRequest);

    boolean checkExistProductInListOrderDetail(List<OrderDetailEntity> orderDetails, ProductEntity product);

    Double getTotalByOrderNumber(Integer orderNumber);
}
