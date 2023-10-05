package com.shopee.clone.DTO.cart;

import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageProductMatchToCart {
    private Long imgProductId;
    private String imgPublicId;
    private String imgProductUrl;
}
