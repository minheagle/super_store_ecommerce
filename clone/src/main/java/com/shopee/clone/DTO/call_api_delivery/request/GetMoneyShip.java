package com.shopee.clone.DTO.call_api_delivery.request;

import lombok.Data;

@Data
public class GetMoneyShip {
    private String addressOfShop;
    private String addressOfUser;
    private Integer quantity;
}
