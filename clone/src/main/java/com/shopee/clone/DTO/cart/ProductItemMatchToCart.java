package com.shopee.clone.DTO.cart;

import com.shopee.clone.DTO.product.ImageProduct;
import com.shopee.clone.DTO.product.response.OptionTypeDTO;
import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductItemMatchToCart {
    private Long pItemId;
    private Double price;
    private Integer qtyInStock;
    private Boolean status;
    private List<ImageProductMatchToCart> imageProductList;
    private List<OptionTypeDTO> optionTypes;
}
