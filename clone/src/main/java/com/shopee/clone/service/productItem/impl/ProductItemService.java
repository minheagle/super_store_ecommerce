package com.shopee.clone.service.productItem.impl;

import com.shopee.clone.DTO.product.ProductItem;
import com.shopee.clone.entity.ProductItemEntity;
import com.shopee.clone.repository.ProductItemRepository;
import com.shopee.clone.service.productItem.IProductItemService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductItemService implements IProductItemService {
    private final ProductItemRepository itemRepository;
    private final ModelMapper modelMapper;

    public ProductItemService(ProductItemRepository itemRepository, ModelMapper modelMapper) {
        this.itemRepository = itemRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductItem createProductItem(ProductItem productItem) {
        return modelMapper.map(itemRepository
                                .save(modelMapper.map(productItem, ProductItemEntity.class)),ProductItem.class);
    }

    @Override
    public ProductItem getProductItemById(Long productId) {
        return modelMapper.map(itemRepository.findById(productId),ProductItem.class);
    }
}
