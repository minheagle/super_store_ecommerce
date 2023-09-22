package com.shopee.clone.DTO.auth.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BecomeSellerRequest {
    private String storeName;
    private String storeAddress;
}
