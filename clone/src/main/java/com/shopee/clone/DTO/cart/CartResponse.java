package com.shopee.clone.DTO.cart;

import com.shopee.clone.DTO.product.ImageProduct;
import com.shopee.clone.DTO.product.OptionType;
import com.shopee.clone.DTO.product.ProductItem;
import com.shopee.clone.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
    private String productName;
    private ProductItem productItem;
    private Double price;
    private Integer quantity;
}
