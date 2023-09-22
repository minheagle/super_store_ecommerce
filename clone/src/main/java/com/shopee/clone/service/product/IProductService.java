package com.shopee.clone.service.product;

import com.shopee.clone.DTO.product.request.ProductRequestCreate;
import com.shopee.clone.DTO.product.update.ProductRequestEdit;
import com.shopee.clone.DTO.product.response.ProductResponseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface IProductService {
    ResponseEntity<?> getAllProductBelongWithShop(Long shopId);
    ResponseEntity<?> getAllProductByCategoryId(Long categoryId);
    ResponseEntity<?> getAllProductPaging(Pageable pageable);
    ResponseEntity<?> getProductById(Long productId);
    ProductResponseDTO getProductByIdForService (Long productId);
    ResponseEntity<?> addNewProduct(ProductRequestCreate productRequest);
    ResponseEntity<?> searchProductByName(String productName);
    ResponseEntity<?> editProductById(Long productId, ProductRequestEdit pRequestEdit);

//This Function might be excuse big change information of product
    ResponseEntity<?> editProductDetailsById(Long productId);
    ResponseEntity<?> removeProductById(Long productId);

}
