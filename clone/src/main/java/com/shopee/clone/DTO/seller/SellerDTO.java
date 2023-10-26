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
    private String imgAvatarPublicId;
    private String storeAvatarUrl;
    private String imgBackgroundPublicId;
    private String storeBackgroundUrl;
    private String storePhoneNumber;
    private String storeBankName;
    private String storeBankAccountNumber;
    private Date createdAt;
    private Integer numberFollower;
    private String chatId;
}
