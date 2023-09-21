package com.shopee.clone.service.product.impl;

import com.shopee.clone.DTO.product.*;
import com.shopee.clone.DTO.product.request.ProductRequestCreate;
import com.shopee.clone.DTO.product.response.*;
import com.shopee.clone.entity.*;
import com.shopee.clone.repository.product.ProductItemRepository;
import com.shopee.clone.repository.product.ProductRepository;
import com.shopee.clone.service.product.IProductService;
import com.shopee.clone.service.productItem.impl.ProductItemService;
import com.shopee.clone.util.ResponseObject;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final ProductItemService itemService;
    private final ProductItemRepository itemRepository;
    private final ModelMapper modelMapper;
    public ProductService(ProductRepository productRepository,
                          ProductItemService itemService,
                          ProductItemRepository itemRepository,
                          ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.itemService = itemService;
        this.itemRepository = itemRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseEntity<?> getAll() {
        return null;
    }

    @Override
    public ResponseEntity<?> getProductById(Long productId) {
        try {
            ProductEntity productEntity = productRepository.findById(productId)
                                .orElseThrow(NoSuchElementException::new);

            List<ProductItemEntity> productItemEntities = productEntity.getProductItemList();
            ProductResponseDTO productResponseDTO = new ProductResponseDTO();
            List<ProductItemResponseDTO> productItemResponseDTOList = new ArrayList<>();

            List<OptionValue> optionValues;
            for(ProductItemEntity productItemEntity : productItemEntities){
                List<ImageProduct> imageProducts = productItemEntity.getImageProductList()
                        .stream()
                        .map(imageProductEntity -> ImageProduct.builder()
                                .imgProductId(imageProductEntity.getImgProductId())
                                .imgPublicId(imageProductEntity.getImgPublicId())
                                .imgProductUrl(imageProductEntity.getImgProductUrl())
                                .build())
                        .collect(Collectors.toList());

                optionValues = productItemEntity.getOptionValues()
                        .stream()
                        .map(optionValueEntity -> OptionValue.builder()
                                .opValueId(optionValueEntity.getOpValueId())
                                .valueName(optionValueEntity.getValueName())
                                .percent_price(optionValueEntity.getPercent_price())
                                .optionType(OptionType

                                        .builder()
                                        .opTypeId(optionValueEntity.getOptionType().getOpTypeId())
                                        .optionName(optionValueEntity.getOptionType().getOptionName())
                                        .build())
                                .build())
                        .collect(Collectors.toList());

                List<OptionTypeDTO> optionTypeDTOS = optionValues
                        .stream()
                        .map(optionValue -> OptionTypeDTO
                                            .builder()
                                            .opTypeId(optionValue.getOptionType().getOpTypeId())
                                            .optionName(optionValue.getOptionType().getOptionName())
                                            .optionValue(OptionValueDTO
                                                    .builder()
                                                    .opValueId(optionValue.getOpValueId())
                                                    .valueName(optionValue.getValueName())
                                                    .percent_price(optionValue.getPercent_price())
                                                    .build())
                                            .build()).collect(Collectors.toList());

                ProductItemResponseDTO productItemResponseDTO = ProductItemResponseDTO
                        .builder()
                        .pItemId(productItemEntity.getPItemId())
                        .price(productItemEntity.getPrice())
                        .qtyInStock(productItemEntity.getQtyInStock())
                        .status(productItemEntity.getStatus())
                        .imageProductList(imageProducts)
                        .optionTypes(optionTypeDTOS)
                        .build();
                productItemResponseDTOList.add(productItemResponseDTO);

                 productResponseDTO = ProductResponseDTO
                        .builder()
                        .productName(productEntity.getProductName())
                        .description(productEntity.getDescription())
                         .status(productEntity.getStatus())
                        .productItemResponseList(productItemResponseDTOList)
                        .build();
            }

            ProductResponseObject<ProductResponseDTO> productResponse = new ProductResponseObject<>();
            productResponse.setData(productResponseDTO);
            if(productResponseDTO.getStatus()){
                return ResponseEntity
                        .status(HttpStatusCode.valueOf(200))
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("Get Product Success")
                                        .results(productResponse)
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
    public ResponseEntity<?> getProductMakeOrderByParentId(Long productId, Long productItemId) {
        try {
            if(productRepository.existsById(productId) && itemRepository.existsById(productItemId)){
                //Get list item by productID
                List<ProductItemEntity> productItems = productRepository.findById(productId).get().getProductItemList();
                //Find productItemId match parameter itemID
                ProductItem itemResult = new ProductItem();
                for(ProductItemEntity itemEntity : productItems){
                    if(itemEntity.getPItemId() == productItemId){
                       itemResult = modelMapper.map(itemEntity,ProductItem.class);
                       break;
                    }
                }
                System.out.println(itemResult.getPItemId());
                ProductResponseObject<ProductItem> itemResponse = new ProductResponseObject<>();
                itemResponse.setData(itemResult);
                   return ResponseEntity
                           .status(HttpStatusCode.valueOf(200))
                           .body(
                                  ResponseObject
                                          .builder()
                                          .status("SUCCESS")
                                          .message("Get Product-Details Success")
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
                                    .status("NOT FOUND")
                                    .message(e.getMessage())
                                    .results("")
                                    .build()
                    );
        }
        return null;
    }

    @Override
    @Transactional
    public ResponseEntity<?> addNewProduct(ProductRequestCreate productRequest) {
        try {
            Product product = Product
                    .builder()
                    .productName(productRequest.getProductName())
                    .description(productRequest.getDescription())
                    .status(true)
//                    .category(productRequest.getCategory())
                    .build();
            Product productAfterSaved = modelMapper.map(
                    productRepository.save(modelMapper.map(product,ProductEntity.class)),Product.class);
            ProductResponseObject<Product> productResponse = new ProductResponseObject<>();
            productResponse.setData(productAfterSaved);
                return ResponseEntity
                        .status(HttpStatusCode.valueOf(200))
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("Add Product Success Pls Do Next-Step: Add Item and list image")
                                        .results(productResponse)
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

    @Override
    public ResponseEntity<?> editProductById(Long productId) {
        return null;
    }

    @Override
    public ResponseEntity<?> editProductDetailsById(Long productId) {
        return null;
    }

    @Override
    public ResponseEntity<?> removeProductById(Long productId) {
        return null;
    }
}
