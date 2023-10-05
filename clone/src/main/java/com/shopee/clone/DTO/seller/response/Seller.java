package com.shopee.clone.DTO.seller.response;

import lombok.*;

import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Seller {
    private Long id;
    private String storeName;
    private String storeAddress;
    private String storeAvatarUrl;
    private String storeBackgroundUrl;
    private Date createdAt;
    private Integer numberFollower = 0;
}
