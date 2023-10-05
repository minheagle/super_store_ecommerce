package com.shopee.clone.DTO.seller;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerDTO {
    private Long id;
    private String storeName;
    private Long userId;
    private String storeAddress;
    private String storeAvatarUrl;
    private String storeBackgroundUrl;
    private Date createdAt;
    private Integer numberFollower;
}
