package com.shopee.clone.service.product.impl;

import com.shopee.clone.DTO.product.*;
import com.shopee.clone.DTO.product.request.ProductRequestCreate;
import com.shopee.clone.DTO.product.response.*;
import com.shopee.clone.entity.*;
import com.shopee.clone.repository.*;
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
    public ResponseEntity<?> getAll(Long shopId) {
        try{
            List<ProductEntity> productEntities = productRepository.findAll();
            List<ProductResponseDTO> productResponseDTOList = new ArrayList<>();

            for(ProductEntity productEntity: productEntities){
                List<ProductItemEntity> productItemEntities = productEntity.getProductItemList();

                List<ProductItemResponseDTO> productItemResponseDTOList = new ArrayList<>();
                for(ProductItemEntity productItemEntity : productItemEntities){

                    mappingSpecialImg_OptionWithProductItem(productItemResponseDTOList, productItemEntity);
                }
                ProductResponseDTO productResponseDTO = ProductResponseDTO
                        .builder()
                        .productName(productEntity.getProductName())
                        .description(productEntity.getDescription())
                        .status(productEntity.getStatus())
                        .productItemResponseList(productItemResponseDTOList)
                        .build();
                productResponseDTOList.add(productResponseDTO);
            }
            ProductResponseObject<List<ProductResponseDTO>> productsResponse = new ProductResponseObject<>();
            productsResponse.setData(productResponseDTOList);

            return ResponseEntity
                    .status(HttpStatusCode.valueOf(200))
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .message("Get Products Success")
                                    .results(productsResponse)
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

    /*
     * Nhận vào List ProductItemResponseDTO Rỗng và itemEntity -> convert Img-OptionType-OptionValue
     * sang ProductItemResponseDTO - sau đó add vào List ProductItemResponseDTO
     * */
    private void mappingSpecialImg_OptionWithProductItem(List<ProductItemResponseDTO> productItemResponseDTOList,
                                                         ProductItemEntity productItemEntity) {
        List<OptionValue> optionValues;
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
    }

    @Override
    public ResponseEntity<?> getProductById(Long productId) {
        try {
            ProductResponseDTO productResponseDTO = getProductByIdForService(productId);
            ProductResponseObject<ProductResponseDTO> productResponse = new ProductResponseObject<>();
                productResponse.setData(productResponseDTO);
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
    public ProductResponseDTO getProductByIdForService(Long productId) {
        try {
            ProductEntity productEntity = productRepository.findById(productId)
                    .orElseThrow(NoSuchElementException::new);
            if (productEntity.getStatus()) {
                List<ProductItemEntity> productItemEntities = productEntity.getProductItemList();
                ProductResponseDTO productResponseDTO = new ProductResponseDTO();
                List<ProductItemResponseDTO> productItemResponseDTOList = new ArrayList<>();

                for (ProductItemEntity productItemEntity : productItemEntities) {

                    mappingSpecialImg_OptionWithProductItem(productItemResponseDTOList, productItemEntity);

                    productResponseDTO = ProductResponseDTO
                            .builder()
                            .productName(productEntity.getProductName())
                            .description(productEntity.getDescription())
                            .status(productEntity.getStatus())
                            .productItemResponseList(productItemResponseDTOList)
                            .build();
                }
                return productResponseDTO;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
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
                        .status(HttpStatusCode.valueOf(201))
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
    @Transactional
    @Override
    public ResponseEntity<?> removeProductById(Long productId) {
        if(productRepository.existsById(productId)){
            ProductEntity productEntity = productRepository.findById(productId)
                    .orElseThrow(NoSuchElementException::new);
            productEntity.setStatus(false);
            productEntity.getProductItemList().stream()
                            .forEach(productItemEntity -> {
                                productItemEntity.setStatus(false);
                                itemRepository.save(productItemEntity);
                            });
            productRepository.save(productEntity);
            return ResponseEntity
                    .status(HttpStatusCode.valueOf(200))
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .message("Product is Removed")
                                    .build()
                    );
        }
        return ResponseEntity
                .status(HttpStatusCode.valueOf(404))
                .body(
                        ResponseObject
                                .builder()
                                .status("FAIL")
                                .message("Product Not Exist!")
                                .build()
                );
    }
}
