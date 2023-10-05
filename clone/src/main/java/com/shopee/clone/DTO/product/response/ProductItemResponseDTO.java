package com.shopee.clone.DTO.product.response;

import com.shopee.clone.DTO.product.ImageProduct;
import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductItemResponseDTO {
    private Long pItemId;
    private Double price;
    private Integer qtyInStock;
    private Boolean status;
    private List<ImageProduct> imageProductList;
    private List<OptionTypeDTO> optionTypes;
}
