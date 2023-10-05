package com.shopee.clone.DTO.cart;

import com.shopee.clone.DTO.seller.response.Seller;
import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private Seller seller;
    private List<LineItem> lineItems;
}
