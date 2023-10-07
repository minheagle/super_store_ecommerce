package com.shopee.clone.service.product_review.img_product_review.impl;

import com.shopee.clone.DTO.upload_file.ImageUploadResult;
import com.shopee.clone.entity.product_review.ImageProductReviewEntity;
import com.shopee.clone.entity.product_review.ProductReviewEntity;
import com.shopee.clone.repository.product_review.ImageProductReviewRepository;
import com.shopee.clone.service.product_review.img_product_review.ImgProductReviewService;
import com.shopee.clone.service.upload_cloud.IUploadImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Service
public class ImgProductReviewServiceImpl implements ImgProductReviewService {
    @Autowired
    private ImageProductReviewRepository imgProductReviewRepository;
    @Autowired
    private IUploadImageService uploadImageService;
    @Value("${cloudinary.comment.folder}")
    private String productReviewImageFolder;

    @Override
    public void saveAllImageProduct(MultipartFile[] imageFiles, ProductReviewEntity productReview) {
        try {
            List<ImageUploadResult> imageUploadResults = uploadImageService
                    .uploadMultiple(imageFiles, productReviewImageFolder);
            List<ImageProductReviewEntity> imageProductReviewEntities =
                    imageUploadResults.stream()
                    .map(result -> ImageProductReviewEntity.builder()
                            .imgPublicId(result.getPublic_id())
                            .imgProductReviewURL(result.getSecure_url())
                            .productReview(productReview)
                            .build())
                    .toList();
            imgProductReviewRepository.saveAll(imageProductReviewEntities);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void delete(List<ImageProductReviewEntity> imageProductReview) {
        imgProductReviewRepository.deleteAll(imageProductReview);
    }

}
