package com.shopee.clone.DTO.payment.requestServe;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class InforReturnStatusPayment {
    Integer code;
    String id;
    Boolean cancel;
    String status;
    Integer orderCode;
}
