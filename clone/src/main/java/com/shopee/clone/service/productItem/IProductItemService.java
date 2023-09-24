package com.shopee.clone.service.productItem;

import com.shopee.clone.DTO.product.request.ProductItemRequest;
import com.shopee.clone.DTO.product.update.ProductItemRequestEdit;
import com.shopee.clone.DTO.product.response.ProductItemResponseDTO;
import org.springframework.http.ResponseEntity;


public interface IProductItemService {
    ResponseEntity<?> createProductItemWithImage(ProductItemRequest productItemRequest);
    ResponseEntity<?> getProductItemByShopIdAndParentProductId(Long productId, Long productItemId);
    ResponseEntity<?> editProductItemById(Long productItemId, ProductItemRequestEdit itemRequestEdit);
    ResponseEntity<?> removeProductItem(Long productId, Long productItemId);
    Boolean checkAvailableQuantityInStock(Long productItemId, Integer qtyMakeOrder);
    Boolean minusQuantityInStock(Long productItemId, Integer qtyMakeOrder);
    ProductItemResponseDTO getProductItemByProductId_ItemId(Long productId, Long productItemId);
}
