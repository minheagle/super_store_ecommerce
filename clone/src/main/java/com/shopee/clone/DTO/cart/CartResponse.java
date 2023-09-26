package com.shopee.clone.DTO.cart;

import com.shopee.clone.DTO.seller.response.Seller;
import lombok.Data;

import java.util.List;
@Data
public class CartResponse {
    private Seller seller;
    private List<LineItem> lineItems;
}
