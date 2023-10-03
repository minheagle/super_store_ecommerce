package com.shopee.clone.DTO.order.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DeliveryInformationRequest {
    private String oderNumber;
    private LocalDateTime orderDate;
    private String recipientName;
    private String deliveryAddress;
    private String phoneNumber;
    private String email;
    private LocalDateTime noteTimeRecipient;
    private List<ItemTransportRequest> itemTransportRequestList;
}
