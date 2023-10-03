package com.shopee.clone.DTO.order.request;

import lombok.Data;

@Data
public class PickupInformationRequest {
    private String pickupAddress;
    private Long shopId;
    private String shopName;
    private String phoneContact;
}