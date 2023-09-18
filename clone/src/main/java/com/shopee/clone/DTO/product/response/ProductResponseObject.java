package com.shopee.clone.DTO.product.response;

import lombok.Data;

@Data
public class ProductResponseObject<T> {
    private T data;
}
