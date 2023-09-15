package com.shopee.clone.service.imageProduct.impl;

import com.shopee.clone.DTO.product.ImageProduct;
import com.shopee.clone.entity.ImageProductEntity;
import com.shopee.clone.repository.ImageProductRepository;
import com.shopee.clone.service.imageProduct.IImageProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageProductService implements IImageProductService {
    private final ImageProductRepository imageProductRepository;
    private final ModelMapper modelMapper;

    public ImageProductService(ImageProductRepository imageProductRepository, ModelMapper modelMapper) {
        this.imageProductRepository = imageProductRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void saveAllImageProduct(List<ImageProduct> imageProducts) {
        List<ImageProductEntity> imageProductEntities = imageProducts.stream()
                .map(imageProduct -> modelMapper.map(imageProduct,ImageProductEntity.class)).collect(Collectors.toList());
        imageProductRepository.saveAll(imageProductEntities);
    }
}
