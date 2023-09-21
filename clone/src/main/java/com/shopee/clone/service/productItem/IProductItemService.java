package com.shopee.clone.service.productItem;

import com.shopee.clone.DTO.product.request.ProductItemRequest;
import org.springframework.http.ResponseEntity;


public interface IProductItemService {
    ResponseEntity<?> createProductItemWithImage(ProductItemRequest productItemRequest);
    ResponseEntity<?> getProductItemByShopIdAndParentProductId(Long productId, Long productItemId);
    Boolean checkAvailableQuantityInStock(Long productItemId, Integer qtyMakeOrder);
}
