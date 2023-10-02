package com.shopee.clone.DTO.order.response;

import com.shopee.clone.DTO.seller.response.Seller;
import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class OrderResponse {
    private Long id;
    private Seller seller;
    private List<OrderDetailResponse> orderDetailList;
    private Double shipMoney;
    private Boolean payment;
    private Date date;
    private String status;
}
