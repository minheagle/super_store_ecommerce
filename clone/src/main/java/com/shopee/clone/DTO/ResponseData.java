package com.shopee.clone.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResponseData<T> {
    private T data;
}
