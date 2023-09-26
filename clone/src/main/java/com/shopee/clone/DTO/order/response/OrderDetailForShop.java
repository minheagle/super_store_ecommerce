package com.shopee.clone.DTO.order.response;

import com.shopee.clone.DTO.seller.response.Seller;
import lombok.Data;

import java.util.List;
@Data
public class OrderDetailForShop {
    private Seller seller;
    private List<OrderDetailResponse> orderDetailList;
    private Double shipMoney;

}
