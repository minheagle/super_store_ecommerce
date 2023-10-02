package com.shopee.clone.DTO.order.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class RawEcommerceRequest {
    private String orderNumber;
    private LocalDateTime orderDate;
    private Double totalAmount;
    private PickupInformationRequest pickupInformationRequest;
    private List<DeliveryInformationRequest> deliveryInformationRequestList;
}
