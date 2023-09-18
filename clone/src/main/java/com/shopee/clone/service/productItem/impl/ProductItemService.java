package com.shopee.clone.service.productItem.impl;

import com.shopee.clone.DTO.product.Product;
import com.shopee.clone.DTO.product.ProductItem;
import com.shopee.clone.DTO.product.request.ProductItemRequest;
import com.shopee.clone.DTO.product.response.ProductResponseObject;
import com.shopee.clone.entity.ProductItemEntity;
import com.shopee.clone.repository.ProductItemRepository;
import com.shopee.clone.repository.ProductRepository;
import com.shopee.clone.service.imageProduct.impl.ImageProductService;
import com.shopee.clone.service.productItem.IProductItemService;
import com.shopee.clone.util.ResponseObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductItemService implements IProductItemService {
    private final ProductItemRepository itemRepository;
    private final ModelMapper modelMapper;
    private final ImageProductService imageProductService;
    @Autowired
    private ProductRepository productRepository;


    public ProductItemService(ProductItemRepository itemRepository, ModelMapper modelMapper, ImageProductService imageProductService) {
        this.itemRepository = itemRepository;
        this.modelMapper = modelMapper;
        this.imageProductService = imageProductService;
    }

    @Override
    public ResponseEntity<?> createProductItemWithImage(ProductItemRequest productItemRequest) {
        try {
            Long productId = productItemRequest.getProductId();;
            if(productRepository.existsById(productId)){
                Product product = modelMapper.map(productRepository.findById(productId), Product.class);
                ProductItem item = ProductItem
                        .builder()
                        .price(productItemRequest.getPrice())
                        .qtyInStock(productItemRequest.getQtyInStock())
                        .status(true)
                        .product(product)
                        .build();
                ProductItem itemSaved = modelMapper.map(
                        itemRepository.save(modelMapper.map(item,ProductItemEntity.class))
                        , ProductItem.class);

                if(itemSaved != null){
                    imageProductService.saveAllImageProduct(productItemRequest.getImgProductFile(), itemSaved);
                }
                ProductResponseObject<ProductItem> itemResponse = new ProductResponseObject<>();
                itemResponse.setData(itemSaved);

                return ResponseEntity
                        .status(HttpStatusCode.valueOf(200))
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("Add ProductItem Success Pls do Next-Step: add OptionType and value")
                                        .results(itemResponse)
                                        .build()
                        );
            }

        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatusCode.valueOf(404))
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("FAIL")
                                    .message(e.getMessage())
                                    .results("")
                                    .build()
                    );
        }
        return null;
    }

    @Override
    public ProductItem getProductItemById(Long productId) {
        return modelMapper.map(itemRepository.findById(productId),ProductItem.class);
    }

    @Override
    public List<ProductItem> saveAll(List<ProductItem> productItems) {
        List<ProductItemEntity> savedProducts = productItems.stream()
                .map(productItem -> modelMapper.map(productItem,ProductItemEntity.class))
                .collect(Collectors.toList());

        return itemRepository.saveAll(savedProducts).stream()
                .map(itemEntity -> modelMapper.map(itemEntity,ProductItem.class))
                .collect(Collectors.toList());
    }
}
