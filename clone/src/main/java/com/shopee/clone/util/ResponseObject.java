package com.shopee.clone.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseObject {
    private String token;
    private String status;
    private String message;
    private Object results;
}
