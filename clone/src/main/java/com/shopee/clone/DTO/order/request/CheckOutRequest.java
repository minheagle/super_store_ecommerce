package com.shopee.clone.DTO.order.request;

import lombok.Data;

import java.util.List;

@Data
public class CheckOutRequest {
    private List<Long> listCartId;
    private String shipAddress;
}
