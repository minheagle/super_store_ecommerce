package com.shopee.clone.DTO.product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageProduct {
    private Long imgProductId;
    private String imgPublicId;
    private String imgProductUrl;
    private ProductItem productItem;
}
