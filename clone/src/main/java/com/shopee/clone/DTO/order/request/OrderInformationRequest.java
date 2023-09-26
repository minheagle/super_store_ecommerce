package com.shopee.clone.DTO.order.request;

import lombok.Data;

import java.util.List;
@Data
public class OrderInformationRequest {
    private String address;
    private List<Long> listCartId;
}
