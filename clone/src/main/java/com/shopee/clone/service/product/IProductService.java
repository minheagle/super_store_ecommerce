package com.shopee.clone.service.product;

import com.shopee.clone.DTO.product.Product;
import com.shopee.clone.DTO.product.request.ProductRequestCreate;
import com.shopee.clone.DTO.product.response.ProductResponseDTO;
import org.springframework.http.ResponseEntity;

public interface IProductService {
    ResponseEntity<?> getAll(Long shopId);
    ResponseEntity<?> getProductById(Long productId);
    ProductResponseDTO getProductByIdForService (Long productId);
    ResponseEntity<?> addNewProduct(ProductRequestCreate productRequest);
    ResponseEntity<?> editProductById(Long productId);

//This Function might be excuse big change information of product
    ResponseEntity<?> editProductDetailsById(Long productId);
    ResponseEntity<?> removeProductById(Long productId);

}
