package com.shopee.clone.service.product;

import com.shopee.clone.DTO.product.Product;
import com.shopee.clone.DTO.product.request.ProductRequestCreate;
import org.springframework.http.ResponseEntity;

public interface IProductService {
    ResponseEntity<?> getAll();
    ResponseEntity<?> getProductById(Long productId);
    ResponseEntity<?> getProductMakeOrderByParentId(Long productId, Long productItemId);
    ResponseEntity<?> addNew(ProductRequestCreate productRequest);
    ResponseEntity<?> editProductById(Long productId);

//This Function might be excuse big change information of product
    ResponseEntity<?> editProductDetailsById(Long productId);
    ResponseEntity<?> removeProductById(Long productId);

}
