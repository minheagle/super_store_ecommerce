package com.shopee.clone.service.product.impl;

import com.shopee.clone.DTO.product.*;
import com.shopee.clone.DTO.product.request.ProductRequestCreate;
import com.shopee.clone.DTO.upload_file.ImageUploadResult;
import com.shopee.clone.entity.*;
import com.shopee.clone.repository.*;
import com.shopee.clone.service.imageProduct.impl.ImageProductService;
import com.shopee.clone.service.optionType.impl.OptionTypeService;
import com.shopee.clone.service.optionValue.impl.OptionValueService;
import com.shopee.clone.service.product.IProductService;
import com.shopee.clone.service.productItem.impl.ProductItemService;
import com.shopee.clone.service.upload_cloud.IUploadImageService;
import com.shopee.clone.util.ResponseObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductItemService itemService;
    @Autowired
    private ProductItemRepository itemRepository;
    @Autowired
    private ImageProductService imageProductService;
    @Autowired
    private OptionTypeService optionTypeService;
    @Autowired
    private OptionValueService optionValueService;
    @Autowired
    private IUploadImageService uploadImageService;
    @Autowired
    private ModelMapper modelMapper;

    @Value("${cloudinary.product.folder}")
    private String productImageFolder;

    @Override
    public ResponseEntity<?> getAll() {
        return null;
    }

    @Override
    public ResponseEntity<?> getProductById(Long productId) {
        try {
            ProductEntity productEntity = productRepository.findById(productId)
                                .orElseThrow(NoSuchElementException::new);
            Product product = modelMapper.map(productEntity,Product.class);
            if(product.getStatus()){
                return ResponseEntity
                        .status(HttpStatusCode.valueOf(200))
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("Get Product Success")
                                        .results(product)
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
                ProductItem productItem = itemService.getProductItemById(productItemId);
                if(productItem.getPItemId() == productItem.getProduct().getProductId()){
                    return ResponseEntity
                            .status(HttpStatusCode.valueOf(200))
                            .body(
                                    ResponseObject
                                            .builder()
                                            .status("SUCCESS")
                                            .message("Get Product-Details Success")
                                            .results(productItem)
                                            .build()
                            );
                }
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
    @Transactional
    public ResponseEntity<?> addNew(ProductRequestCreate productRequest) {
        try {
            Product product = Product
                    .builder()
                    .productName(productRequest.getProductName())
                    .description(productRequest.getDescription())
                    .status(true)
                    .build();
            ProductEntity productEntity = modelMapper.map(product, ProductEntity.class);
            ProductEntity productAfterSaved = productRepository.save(productEntity);

            if(productAfterSaved != null){
                ProductItem productItem = ProductItem
                        .builder()
                        .price(productRequest.getPrice())
                        .qtyInStock(productRequest.getQtyInStock())
                        .status(true)
                        .product(modelMapper.map(productAfterSaved, Product.class))
                        .build();
                ProductItem itemAfterSaved = itemService.createProductItem(productItem);

                if(itemAfterSaved != null && productRequest.getImgProductFile() != null){
                    List<ImageUploadResult> imageUploadResults = uploadImageService
                            .uploadMultiple(productRequest.getImgProductFile(), productImageFolder);
                    List<ImageProduct> imgProducts = imageUploadResults.stream()
                            .map(result -> ImageProduct.builder()
                                    .imgPublicId(result.getPublic_id())
                                    .imgProductUrl(result.getSecure_url())
                                    .productItem(modelMapper.map(itemAfterSaved, ProductItem.class))
                                    .build())
                            .collect(Collectors.toList());

                    imageProductService.saveAllImageProduct(imgProducts);
                }
                if(!productRequest.getOptionName().isBlank()){
                    OptionType optionType = OptionType
                            .builder()
                            .optionName(productRequest.getOptionName())
                            .productItem(modelMapper.map(itemAfterSaved, ProductItem.class))
                            .build();
                    OptionType optionTypeAfterSaved = optionTypeService.createOptionType(optionType);

                    if(optionTypeAfterSaved != null){
                        OptionValue optionValue = OptionValue
                                .builder()
                                .valueName(productRequest.getValueName())
                                .percent_price(productRequest.getPercent_price())
                                .optionType(modelMapper.map(optionTypeAfterSaved, OptionType.class))
                                .build();
                        optionValueService.createOptionValue(optionValue);
                    }
                }
                return ResponseEntity
                        .status(HttpStatusCode.valueOf(200))
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("Add New Product Success")
                                        .results("")
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
