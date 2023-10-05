package com.shopee.clone.response.province;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListAllProvinceResponse<T> {
    private T data;
    private Integer count;
}
