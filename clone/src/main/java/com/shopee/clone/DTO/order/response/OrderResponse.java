package com.shopee.clone.DTO.order.response;

import com.shopee.clone.DTO.seller.response.Seller;
import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class OrderResponse {
    private Long id;
    private List<OrderDetailForShop> orderDetails;
    private Date date;
    private String paymentMethod;
    private String status;
}
