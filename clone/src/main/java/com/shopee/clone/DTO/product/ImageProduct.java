package com.shopee.clone.DTO.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
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
