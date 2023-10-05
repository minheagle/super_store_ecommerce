package com.shopee.clone.response.province;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListAllWardByDistrictResponse<T> {
    private T data;
    private Integer count;
}
