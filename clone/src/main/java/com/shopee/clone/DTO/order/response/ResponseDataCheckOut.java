package com.shopee.clone.DTO.order.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ResponseDataCheckOut<T, K> {
    private  T data;
    private  K orderNumber;
}
