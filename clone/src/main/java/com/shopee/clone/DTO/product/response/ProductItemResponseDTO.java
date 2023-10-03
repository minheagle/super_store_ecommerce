package com.shopee.clone.DTO.product.response;

import com.shopee.clone.DTO.product.ImageProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductItemResponseDTO {
    private Long pItemId;
    private Boolean status;
    private List<ImageProduct> imageProductList;
    private List<OptionTypeDTO> optionTypes;
}
