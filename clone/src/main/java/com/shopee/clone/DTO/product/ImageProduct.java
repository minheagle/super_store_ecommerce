package com.shopee.clone.DTO.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private ProductItem productItem;
}
