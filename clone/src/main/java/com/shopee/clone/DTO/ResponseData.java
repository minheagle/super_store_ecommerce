package com.shopee.clone.DTO;

import lombok.Data;
@Data
public class ResponseData<T> {
    private T data;
}
