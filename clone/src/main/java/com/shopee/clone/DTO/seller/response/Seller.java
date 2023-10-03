package com.shopee.clone.DTO.seller.response;

import lombok.Data;

import java.util.Date;
@Data
public class Seller {
    private Long id;
    private String storeName;
    private String storeAddress;
    private String storeAvatarUrl;
    private String storeBackgroundUrl;
    private Date createdAt;
    private Integer numberFollower = 0;
}
