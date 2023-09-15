package com.shopee.clone.service.productItem;

import com.shopee.clone.DTO.product.ProductItem;

public interface IProductItemService {
    ProductItem createProductItem(ProductItem productItem);
    ProductItem getProductItemById(Long productId);
}
