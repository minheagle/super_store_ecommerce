package com.shopee.clone.service.product_review.impl;

import com.shopee.clone.DTO.ResponseData;
import com.shopee.clone.DTO.product_review.ProductReviewRequest;
import com.shopee.clone.DTO.product_review.ProductReviewResponse;
import com.shopee.clone.DTO.product_review.ProductReviewUpdateRequest;
import com.shopee.clone.entity.ProductEntity;
import com.shopee.clone.entity.UserEntity;
import com.shopee.clone.entity.order.EOrder;
import com.shopee.clone.entity.order.OrderDetailEntity;
import com.shopee.clone.entity.order.OrderEntity;
import com.shopee.clone.entity.product_review.ImageProductReviewEntity;
import com.shopee.clone.entity.product_review.ProductReviewEntity;
import com.shopee.clone.repository.order.OrderDetailRepository;
import com.shopee.clone.repository.order.OrderRepository;
import com.shopee.clone.repository.product.ProductRepository;
import com.shopee.clone.repository.product_review.ProductReviewRepository;
import com.shopee.clone.service.order.OrderService;
import com.shopee.clone.service.product_review.ProductReviewService;
import com.shopee.clone.service.product_review.img_product_review.ImgProductReviewService;
import com.shopee.clone.service.user.UserService;
import com.shopee.clone.util.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductReviewServiceImpl implements ProductReviewService {
    @Autowired
    private ProductReviewRepository productReviewRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ImgProductReviewService imgProductReviewService;

    @Override
    public ResponseEntity<?> createRating(ProductReviewRequest productReviewRequest) {
        try {
            UserEntity user = userService.findUserByID(productReviewRequest.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            ProductEntity product = productRepository.findById(productReviewRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            Optional<ProductReviewEntity> productReview = productReviewRepository.findByProductAndUser(product, user);
            if (productReview.isEmpty()) {
                OrderEntity orderEntity = orderRepository.findById(productReviewRequest.getOrderId())
                        .orElseThrow(() -> new RuntimeException("Order not found"));
                if (orderEntity.getStatus().equals(EOrder.Completed)) {
                    boolean check = orderService.checkExistProductInListOrderDetail(orderEntity.getOrderDetails(), product);
                    if (check) {
                        ProductReviewEntity productReviewEntity =
                                ProductReviewEntity.
                                        builder()
                                        .user(user)
                                        .comment(productReviewRequest.getComment())
                                        .product(product)
                                        .rating(productReviewRequest.getVoteStar())
                                        .build();

                        ProductReviewEntity finalProductReview = productReviewRepository.save(productReviewEntity);
                        imgProductReviewService.saveAllImageProduct(productReviewRequest.getImageProductReviewFile(), finalProductReview);
                        setRatingProduct(productReviewRequest.getProductId());
                        return ResponseEntity
                                .status(HttpStatusCode.valueOf(200))
                                .body(
                                        ResponseObject
                                                .builder()
                                                .status("SUCCESS")
                                                .message("Create rating success!")
                                                .results("")
                                                .build()
                                );
                    }
                    return ResponseEntity
                            .status(HttpStatusCode.valueOf(404))
                            .body(
                                    ResponseObject
                                            .builder()
                                            .status("FAIL")
                                            .message("Product haven't in order!")
                                            .results("You kidding me?")
                                            .build()
                            );
                }
                return ResponseEntity
                        .status(HttpStatusCode.valueOf(404))
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("FAIL")
                                        .message("You not rating when order success!")
                                        .results("")
                                        .build()
                        );

            }
        } catch (Exception e) {
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
    public ResponseEntity<?> getRating(Long pReview) {
        try {
            Optional<ProductReviewEntity> productReviewEntity = productReviewRepository.findById(pReview);
            if (productReviewEntity.isPresent()) {
                ProductReviewEntity productReview = productReviewEntity.get();

                ProductReviewResponse productReviewResponse = convertToProductReviewResponse(productReview);
                ResponseData<Object> data = ResponseData.builder().data(productReviewResponse).build();
                return ResponseEntity
                        .status(HttpStatusCode.valueOf(200))
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("Get rating success!")
                                        .results(data)
                                        .build()
                        );
            }
        } catch (Exception e) {
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
    public ResponseEntity<?> getALlByProduct(Long productId) {
        try {
            Optional<ProductEntity> productEntity = productRepository.findById(productId);
            if (productEntity.isPresent()) {
                ProductEntity product = productEntity.get();

                List<ProductReviewEntity> productReviews = productReviewRepository.findByProduct(product);
                List<ProductReviewResponse> productReviewResponses = new ArrayList<>();
                productReviews.forEach(productReview -> {
                    ProductReviewResponse productReviewResponse = convertToProductReviewResponse(productReview);
                    productReviewResponses.add(productReviewResponse);
                });
                ResponseData<Object> data = ResponseData.builder().data(productReviewResponses).build();
                return ResponseEntity
                        .status(HttpStatusCode.valueOf(200))
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("Get all rating by product success!")
                                        .results(data)
                                        .build()
                        );
            }
        } catch (Exception e) {
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
    public ResponseEntity<?> deleteProductReview(Long productReviewId) {
        try {
            Optional<ProductReviewEntity> productReviewEntity = productReviewRepository.findById(productReviewId);
            if (productReviewEntity.isPresent()) {
                ProductReviewEntity productReview = productReviewEntity.get();
                imgProductReviewService.delete(productReview.getImageProductReview());
                productReviewRepository.delete(productReview);
                setRatingProduct(productReview.getProduct().getProductId());
                return ResponseEntity
                        .status(HttpStatusCode.valueOf(200))
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("Delete product review success!")
                                        .results("")
                                        .build()
                        );
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatusCode.valueOf(401))
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

    private void setRatingProduct(Long productId) {
        List<ProductReviewEntity> reviews = productReviewRepository.findByProduct_ProductId(productId);
        Optional<ProductEntity> productEntity = productRepository.findById(productId);
        if (productEntity.isPresent()) {
            ProductEntity product = productEntity.get();

            if (reviews.isEmpty()) {
                product.setVoteStar(null);
            }

            int totalRating = 0;

            for (ProductReviewEntity review : reviews) {
                totalRating += review.getRating();
            }
            product.setVoteStar((double) totalRating / reviews.size());
            productRepository.save(product);
        }
    }

    @Override
    public ResponseEntity<?> updateRating(ProductReviewUpdateRequest productReviewUpdateRequest) {
        try {
            Optional<ProductReviewEntity> productReviewEntity = productReviewRepository.findById(productReviewUpdateRequest.getProductReviewId());
            if (productReviewEntity.isPresent()) {
                ProductReviewEntity productReview = productReviewEntity.get();
                productReview.setRating(productReview.getRating());
                productReview.setComment(productReview.getComment());
                ProductReviewEntity finalProductReview = productReviewRepository.save(productReview);
                setRatingProduct(productReview.getProduct().getProductId());
                ProductReviewResponse productReviewResponse = convertToProductReviewResponse(finalProductReview);
                ResponseData<Object> data = ResponseData.builder().data(productReviewResponse).build();
                return ResponseEntity
                        .status(HttpStatusCode.valueOf(200))
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("Update rating success!")
                                        .results(data)
                                        .build()
                        );
            }
        } catch (Exception e) {
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
    public boolean checkRating(Long userId, Long productId) {
        Optional<ProductReviewEntity> productReview = productReviewRepository.findByProduct_ProductIdAndUser_Id(productId, userId);
        if (productReview.isPresent()) {
            return true;
        }
        return false;
    }

    private ProductReviewResponse convertToProductReviewResponse(ProductReviewEntity productReview) {
        ProductReviewResponse productReviewResponse = new ProductReviewResponse();
        productReviewResponse.setId(productReview.getId());
        productReviewResponse.setComment(productReview.getComment());
        productReviewResponse.setUserId(productReview.getUser().getId());
        productReviewResponse.setFullName(productReview.getUser().getFullName());
        productReviewResponse.setImgAvatar(productReview.getUser().getImageUrl());
        productReviewResponse.setVoteStar(productReview.getRating());
        List<String> listImageRating =
                productReview.getImageProductReview().stream().map(ImageProductReviewEntity::getImgProductReviewURL).toList();
        productReviewResponse.setImgURL(listImageRating);
        return productReviewResponse;
    }
}