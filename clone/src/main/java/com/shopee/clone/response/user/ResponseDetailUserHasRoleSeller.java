package com.shopee.clone.response.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDetailUserHasRoleSeller<T, K> {
    private T data;
    private K shopData;
}
