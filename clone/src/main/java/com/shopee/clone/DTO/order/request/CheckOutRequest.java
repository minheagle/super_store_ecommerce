package com.shopee.clone.DTO.order.request;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckOutRequest {
    private List<Long> listCartId;
    private String shipAddress;
}
