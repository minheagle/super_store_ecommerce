package com.shopee.clone.service.imageProduct.impl;

import com.shopee.clone.DTO.product.ImageProduct;
import com.shopee.clone.DTO.product.ProductItem;
import com.shopee.clone.DTO.product.update.SingleUpdateChangeImageProductItem;
import com.shopee.clone.DTO.upload_file.ImageUploadResult;
import com.shopee.clone.entity.ImageProductEntity;
import com.shopee.clone.repository.product.ImageProductRepository;
import com.shopee.clone.service.imageProduct.IImageProductService;
import com.shopee.clone.service.upload_cloud.IUploadImageService;
import com.shopee.clone.util.ResponseObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ImageProductService implements IImageProductService {
    private final ImageProductRepository imageProductRepository;
    private final ModelMapper modelMapper;
    private final IUploadImageService uploadImageService;
    @Value("${cloudinary.product.folder}")
    private String productImageFolder;

    public ImageProductService(ImageProductRepository imageProductRepository, ModelMapper modelMapper, IUploadImageService uploadImageService) {
        this.imageProductRepository = imageProductRepository;
        this.modelMapper = modelMapper;
        this.uploadImageService = uploadImageService;
    }

    @Override
    public void saveAllImageProduct(MultipartFile[] imageFiles, ProductItem productItem) {
        try {
            List<ImageUploadResult> imageUploadResults = uploadImageService
                    .uploadMultiple(imageFiles, productImageFolder);
            List<ImageProduct> imageProducts = imageUploadResults.stream()
                    .map(result -> ImageProduct.builder()
                            .imgPublicId(result.getPublic_id())
                            .imgProductUrl(result.getSecure_url())
                            .productItem(productItem)
                            .build()).toList();
            List<ImageProductEntity> imageProductEntities = imageProducts.stream()
                    .map(imageProduct -> modelMapper.map(imageProduct,ImageProductEntity.class)).collect(Collectors.toList());
            imageProductRepository.saveAll(imageProductEntities);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    @Override
    public ResponseEntity<?> changeImageProduct(SingleUpdateChangeImageProductItem changeImageProductItem) {
        try {
            if(imageProductRepository.existsById(changeImageProductItem.getImageProductItemId())){
                if(!changeImageProductItem.getFile().isEmpty()){
                    ImageProductEntity imageProductEntity = imageProductRepository.findById(changeImageProductItem.getImageProductItemId())
                            .orElseThrow(NoSuchElementException::new);
                    ImageUploadResult imageUploadResult =  uploadImageService.replaceSingle(changeImageProductItem.getFile(),
                                                                                            changeImageProductItem.getImagePublicId(),
                                                                                            productImageFolder);
                    imageProductEntity.setImgProductUrl(imageUploadResult.getSecure_url());
                    imageProductEntity.setImgPublicId(imageUploadResult.getPublic_id());
                    imageProductRepository.save(imageProductEntity);
                    return ResponseEntity
                            .ok()
                            .body(
                                    ResponseObject
                                            .builder()
                                            .status("SUCCESS")
                                            .message("Update success")
                                            .results("")
                                            .build()
                            );
                }

            }

        }catch (Exception e){
            return ResponseEntity
                    .badRequest()
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
}
