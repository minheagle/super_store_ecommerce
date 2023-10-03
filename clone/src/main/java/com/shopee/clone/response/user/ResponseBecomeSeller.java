package com.shopee.clone.response.user;

import lombok.Data;

@Data
public class ResponseBecomeSeller<T, K> {
    private T data;
    private K shopData;
}
