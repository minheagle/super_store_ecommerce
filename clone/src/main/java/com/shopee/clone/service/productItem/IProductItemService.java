package com.shopee.clone.service.productItem;

import com.shopee.clone.DTO.product.Product;
import com.shopee.clone.DTO.product.ProductItem;
import com.shopee.clone.DTO.product.request.ProductItemRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IProductItemService {
    ResponseEntity<?> createProductItemWithImage(ProductItemRequest productItemRequest);
    ProductItem getProductItemById(Long productId);
    List<ProductItem> saveAll(List<ProductItem> productItems);
}
