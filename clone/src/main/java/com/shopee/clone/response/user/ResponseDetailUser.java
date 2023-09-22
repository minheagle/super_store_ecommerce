package com.shopee.clone.response.user;

import lombok.Data;

@Data
public class ResponseDetailUser<T> {
    private T data;
}
