package com.shopee.clone.service.product.Impl;

import com.shopee.clone.DTO.product.ImageProduct;
import com.shopee.clone.DTO.product.Product;
import com.shopee.clone.DTO.product.ProductItem;
import com.shopee.clone.DTO.product.request.ProductRequestCreate;
import com.shopee.clone.DTO.upload_file.ImageUploadResult;
import com.shopee.clone.entity.ImageProductEntity;
import com.shopee.clone.entity.ProductEntity;
import com.shopee.clone.entity.ProductItemEntity;
import com.shopee.clone.repository.ImageProductRepository;
import com.shopee.clone.repository.ProductItemRepository;
import com.shopee.clone.repository.ProductRepository;
import com.shopee.clone.service.product.IProductService;
import com.shopee.clone.service.upload_cloud.IUploadImageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductItemRepository itemRepository;
    @Autowired
    private ImageProductRepository imageProductRepository;
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
        return null;
    }

    @Override
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
                ProductItemEntity productItemEntity = modelMapper.map(productItem, ProductItemEntity.class);
                ProductItemEntity itemAfterSaved = itemRepository.save(productItemEntity);

                if(itemAfterSaved != null){
                    List<ImageUploadResult> imageUploadResults = uploadImageService
                            .uploadMultiple(productRequest.getImgProductFile(), productImageFolder);
                    List<ImageProduct> imgProducts = imageUploadResults.stream()
                            .map(result -> ImageProduct.builder()
                                    .imgPublicId(result.getPublic_id())
                                    .imgProductUrl(result.getSecure_url())
                                    .productItem(modelMapper.map(itemAfterSaved, ProductItem.class))
                                    .build())
                            .collect(Collectors.toList());

                    List<ImageProductEntity> imgProductEntities = imgProducts.stream()
                            .map(imageProduct -> modelMapper.map(imageProduct, ImageProductEntity.class))
                            .collect(Collectors.toList());

                    imageProductRepository.saveAll(imgProductEntities);
                }
            }

        }catch (Exception e){

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
