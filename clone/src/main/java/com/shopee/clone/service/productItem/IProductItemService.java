package com.shopee.clone.service.productItem;

import com.shopee.clone.DTO.product.ProductItem;
import com.shopee.clone.DTO.product.request.OptionTypeCreate;
import com.shopee.clone.DTO.product.request.ProductItemFullOptionRequest;
import com.shopee.clone.DTO.product.request.ProductItemRequest;
import com.shopee.clone.DTO.product.update.ProductItemRequestEdit;
import com.shopee.clone.DTO.product.response.ProductItemResponseDTO;
import com.shopee.clone.entity.ProductItemEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface IProductItemService {
    ResponseEntity<?> createProductItemWithImage(ProductItemRequest productItemRequest);
    ResponseEntity<?> createProductItemFullOption(ProductItemFullOptionRequest productItemFullOptionRequest);
    ResponseEntity<?> getProductItemByShopIdAndParentProductId(Long productId, Long productItemId);
    ResponseEntity<?> editProductItemById(Long productItemId, ProductItemRequestEdit itemRequestEdit);
    ResponseEntity<?> removeProductItem(Long productId, Long productItemId);
    Double findMinPriceInProductItem(List<ProductItemEntity> productItems);
    Double findMaxPriceInProductItem(List<ProductItemEntity> productItems);
    Boolean checkAvailableQuantityInStock(Long productItemId, Integer qtyMakeOrder);
    Boolean minusQuantityInStock(Long productItemId, Integer qtyMakeOrder);
    Boolean plusQuantityInStock(Long productItemId, Integer qtyMakeOrder);
    ProductItemResponseDTO getProductItemByProductId_ItemId(Long productId, Long productItemId);
}
