package com.shopee.clone.DTO.order.request;

import lombok.Data;

import java.util.List;
@Data
public class RawEcommerceRequest {
    private Double totalAmount;
    private PickupInformationRequest pickupInformationRequest;
    private List<DeliveryInformationRequest> deliveryInformationRequestList;
}