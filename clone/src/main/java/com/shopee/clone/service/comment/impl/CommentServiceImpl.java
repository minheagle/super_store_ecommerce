package com.shopee.clone.service.comment.impl;

import com.shopee.clone.DTO.comment.request.CreateCommentRequest;
import com.shopee.clone.entity.ProductEntity;
import com.shopee.clone.entity.UserEntity;
import com.shopee.clone.entity.comment.CommentEntity;
import com.shopee.clone.repository.comment.CommentRepository;
import com.shopee.clone.repository.product.ProductRepository;
import com.shopee.clone.service.comment.CommentService;
import com.shopee.clone.service.product.impl.ProductService;
import com.shopee.clone.service.user.UserService;
import com.shopee.clone.util.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductRepository productRepository;
    @Override
    public ResponseEntity<?> createComment(CreateCommentRequest createCommentRequest) {
        try {
            Optional<UserEntity> userEntity =  userService.findUserByID(createCommentRequest.getUserId());
            Optional<ProductEntity> productEntity = productRepository.findById(createCommentRequest.getProductId());
            CommentEntity parentComment = null;
            if(createCommentRequest.getParentId()!=null){
                Optional<CommentEntity> commentEntity = commentRepository.findById(createCommentRequest.getParentId());
                if(commentEntity.isPresent()){
                    parentComment = commentEntity.get();
                }
            }
            if(userEntity.isPresent() && productEntity.isPresent()){
                UserEntity user = userEntity.get();
                ProductEntity product = productEntity.get();
                CommentEntity comment = new CommentEntity();
                comment.setParentComment(parentComment);
                comment.setUser(user);
                comment.setProduct(product);
                comment.setContent(createCommentRequest.getContent());
//              Lưu comment
                commentRepository.save(comment);
            }
        }catch (Exception e){
                return ResponseEntity
                        .badRequest()
                        .body(ResponseObject.builder()
                                .status("FAIL")
                                .message(e.getMessage())
                                .results("")
                                .build()
                        );
    }
}
