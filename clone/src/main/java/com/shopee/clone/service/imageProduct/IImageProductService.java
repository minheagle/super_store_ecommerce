package com.shopee.clone.service.imageProduct;

import com.shopee.clone.DTO.product.ImageProduct;
import com.shopee.clone.DTO.product.ProductItem;
import com.shopee.clone.entity.ImageProductEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageProductService {
    void saveAllImageProduct(MultipartFile[] itemImage, ProductItem productItem);
}
