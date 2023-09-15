package com.shopee.clone.DTO.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageProduct {
    private Long imgProductId;
    private String imgPublicId;
    private String imgProductUrl;
    private ProductItem productItem;
}
