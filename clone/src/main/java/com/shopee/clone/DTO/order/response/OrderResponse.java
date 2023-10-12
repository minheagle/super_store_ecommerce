package com.shopee.clone.DTO.order.response;

import com.shopee.clone.DTO.seller.response.Seller;
import lombok.*;

import java.util.Date;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private Seller seller;
    private Integer orderNumber;
    private List<OrderDetailResponse> orderDetailList;
    private Double amount;
    private Double shipMoney;
    private Double discount;
    private Double total;
    private String deliveryAddress;
    private Boolean payment;
    private Date date;
    private String status;
}
