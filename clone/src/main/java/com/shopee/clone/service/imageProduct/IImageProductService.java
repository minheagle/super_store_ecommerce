package com.shopee.clone.service.imageProduct;

import com.shopee.clone.DTO.product.ImageProduct;
import com.shopee.clone.DTO.product.ProductItem;
import com.shopee.clone.DTO.product.update.SingleUpdateChangeImageProductItem;
import com.shopee.clone.entity.ImageProductEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageProductService {
    void saveAllImageProduct(MultipartFile[] itemImage, ProductItem productItem);
    ResponseEntity<?> changeImageProduct(SingleUpdateChangeImageProductItem changeImageProductItem);
}
