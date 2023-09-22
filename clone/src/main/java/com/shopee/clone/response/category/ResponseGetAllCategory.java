package com.shopee.clone.response.category;

import lombok.Data;

@Data
public class ResponseGetAllCategory<T> {
    private T data;
    private Integer totalCount;
}
