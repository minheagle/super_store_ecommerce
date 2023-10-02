package com.shopee.clone.DTO.order.request;

import lombok.Data;

import java.util.List;

@Data
public class DeliveryInformationRequest {
    private String recipientName;
    private String deliveryAddress;
    private String phoneNumber;
    private String email;
    private List<ItemTransportRequest> itemTransportRequestList;
}
