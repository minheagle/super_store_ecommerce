package com.shopee.clone.response.auth.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseLoginHasRoleSeller<T, K> {
    private T data;
    private K shopData;
    private String accessToken;
    private String refreshToken;
}
