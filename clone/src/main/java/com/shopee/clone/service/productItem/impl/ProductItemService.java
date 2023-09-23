package com.shopee.clone.service.productItem.impl;

import com.shopee.clone.DTO.product.*;
import com.shopee.clone.DTO.product.request.OptionTypeRequest;
import com.shopee.clone.DTO.product.request.ProductItemRequest;
import com.shopee.clone.DTO.product.update.ProductItemRequestEdit;
import com.shopee.clone.DTO.product.response.OptionTypeDTO;
import com.shopee.clone.DTO.product.response.OptionValueDTO;
import com.shopee.clone.DTO.product.response.ProductItemResponseDTO;
import com.shopee.clone.DTO.product.response.ProductResponseObject;
import com.shopee.clone.entity.OptionValueEntity;
import com.shopee.clone.entity.ProductItemEntity;
import com.shopee.clone.repository.product.OptionValueRepository;
import com.shopee.clone.repository.product.ProductItemRepository;
import com.shopee.clone.repository.product.ProductRepository;
import com.shopee.clone.service.imageProduct.impl.ImageProductService;
import com.shopee.clone.service.optionValue.impl.OptionValueService;
import com.shopee.clone.service.productItem.IProductItemService;
import com.shopee.clone.util.ResponseObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ProductItemService implements IProductItemService {
    private final ProductItemRepository itemRepository;
    private final ModelMapper modelMapper;
    private final ImageProductService imageProductService;
    private final ProductRepository productRepository;
    private final OptionValueRepository optionValueRepository;

    public ProductItemService(ProductItemRepository itemRepository,
                              ModelMapper modelMapper,
                              ImageProductService imageProductService,
                              ProductRepository productRepository,
                              OptionValueRepository optionValueRepository) {
        this.itemRepository = itemRepository;
        this.modelMapper = modelMapper;
        this.imageProductService = imageProductService;
        this.productRepository = productRepository;
        this.optionValueRepository = optionValueRepository;
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
                        .status(HttpStatusCode.valueOf(201))
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("Add ProductItem Success Pls go Next-Step: add OptionType and value")
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
    public ResponseEntity<?> getProductItemByShopIdAndParentProductId(Long productId, Long productItemId) {
        try {
            ProductItemResponseDTO productItemResponseDTO = getProductItemByProductId_ItemId(productId, productItemId);
            if(productItemResponseDTO == null){
                return ResponseEntity
                        .status(HttpStatusCode.valueOf(204))
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("Status's Item: False")
                                        .build()
                        );
            }
            ProductResponseObject<ProductItemResponseDTO> itemResponseDTO = new ProductResponseObject<>();
                itemResponseDTO.setData(productItemResponseDTO);

                return ResponseEntity
                        .status(HttpStatusCode.valueOf(200))
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("Get Item Successfully")
                                        .results(itemResponseDTO)
                                        .build()
                        );
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
    }

    @Transactional
    @Override
    public ResponseEntity<?> editProductItemById(Long productItemId, ProductItemRequestEdit itemRequestEdit) {
        try {
            if(itemRepository.existsById(productItemId)){
                ProductItemEntity productItem = itemRepository.findById(productItemId)
                        .orElseThrow(NoSuchElementException::new);

                List<OptionValueEntity> optionValueEntities = productItem.getOptionValues();
                List<OptionTypeRequest> optionTypeRequests = itemRequestEdit.getOptionTypes();

                optionValueEntities.forEach(optionValue -> {
                    optionTypeRequests.stream()
                            .filter(optionTypeRequest -> optionValue.getOptionType().getOptionName()
                                                                                    .equals(optionTypeRequest.getOptionName()))
                            .findFirst()
                            .ifPresent(optionTypeRequest -> {
                                optionValue.setValueName(optionTypeRequest.getOptionValueRequest().getValueName());
                                optionValueRepository.save(optionValue);
                            });
                });

                productItem.setPrice(itemRequestEdit.getPrice());
                productItem.setQtyInStock(itemRequestEdit.getQtyInStock());
                productItem.setOptionValues(optionValueEntities);
                itemRepository.save(productItem);

                return ResponseEntity
                        .status(HttpStatusCode.valueOf(200))
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("Product Item was Updated")
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
                                    .message("Product Item Not Exist!")
                                    .build()
                    );
        }
        return null;
    }

    @Override
    public ResponseEntity<?> removeProductItem(Long productId, Long productItemId) {
        ProductItemEntity productItem = itemRepository.findById(productItemId)
                .orElseThrow(NoSuchElementException::new);
        if(productRepository.existsById(productId) && productItem.getProduct().getProductId().equals(productId)){
            productItem.setStatus(false);
            itemRepository.save(productItem);
            return ResponseEntity
                    .status(HttpStatusCode.valueOf(200))
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .message("ProductItem is Removed")
                                    .build()
                    );
        }

        return ResponseEntity
                .status(HttpStatusCode.valueOf(404))
                .body(
                        ResponseObject
                                .builder()
                                .status("FAIL")
                                .message("ProductItem Not Exist!")
                                .build()
                );
    }

    @Override
    public Boolean checkAvailableQuantityInStock(Long productItemId, Integer qtyMakeOrder) {
        ProductItemEntity productItem = itemRepository.findById(productItemId)
                .orElseThrow(NoSuchElementException::new);
        if(productItem.getQtyInStock() >= qtyMakeOrder){
            productItem.setQtyInStock(productItem.getQtyInStock() - qtyMakeOrder);
            itemRepository.save(productItem);
            return true;
        }
        return false;
    }

    @Override
    public ProductItemResponseDTO getProductItemByProductId_ItemId(Long productId, Long productItemId) {
        ProductItemEntity productItem = itemRepository.findById(productItemId).orElseThrow(NoSuchElementException::new);
        if(productRepository.existsById(productId) && productItem.getStatus()
                && productItem.getProduct().getProductId().equals(productId)){
            List<OptionTypeDTO> optionTypeDTOList = productItem.getOptionValues()
                    .stream()
                    .map(optionValue -> OptionTypeDTO.builder()
                            .opTypeId(optionValue.getOptionType().getOpTypeId())
                            .optionName(optionValue.getOptionType().getOptionName())
                            .optionValue(OptionValueDTO.builder()
                                    .opValueId(optionValue.getOpValueId())
                                    .valueName(optionValue.getValueName())
                                    .build())
                            .build()).collect(Collectors.toList());

            List<ImageProduct> imageProducts = productItem.getImageProductList()
                    .stream()
                    .map(imageProductEntity -> ImageProduct.builder()
                            .imgProductId(imageProductEntity.getImgProductId())
                            .imgPublicId(imageProductEntity.getImgPublicId())
                            .imgProductUrl(imageProductEntity.getImgProductUrl())
                            .build())
                    .collect(Collectors.toList());

            ProductItemResponseDTO productItemResponseDTO = ProductItemResponseDTO
                    .builder()
                    .pItemId(productItem.getPItemId())
                    .price(productItem.getPrice())
                    .qtyInStock(productItem.getQtyInStock())
                    .status(productItem.getStatus())
                    .imageProductList(imageProducts)
                    .optionTypes(optionTypeDTOList)
                    .build();
            return productItemResponseDTO;
        }
        return null;
    }
}
