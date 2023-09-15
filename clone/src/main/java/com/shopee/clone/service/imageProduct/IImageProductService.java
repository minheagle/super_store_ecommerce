package com.shopee.clone.service.imageProduct;

import com.shopee.clone.DTO.product.ImageProduct;

import java.util.List;

public interface IImageProductService {
    void saveAllImageProduct(List<ImageProduct> imageProducts);
}
