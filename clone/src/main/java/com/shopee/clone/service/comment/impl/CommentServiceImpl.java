package com.shopee.clone.service.comment.impl;

import com.shopee.clone.DTO.ResponseData;
import com.shopee.clone.DTO.comment.CommentDTO;
import com.shopee.clone.DTO.comment.request.CreateCommentRequest;
import com.shopee.clone.entity.ProductEntity;
import com.shopee.clone.entity.UserEntity;
import com.shopee.clone.entity.comment.CommentEntity;
import com.shopee.clone.repository.comment.CommentRepository;
import com.shopee.clone.repository.product.ProductRepository;
import com.shopee.clone.service.comment.CommentService;
import com.shopee.clone.service.user.UserService;
import com.shopee.clone.util.ResponseObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public ResponseEntity<?> createComment(CreateCommentRequest createCommentRequest) {
        try {
            Optional<UserEntity> userEntity = userService.findUserByID(createCommentRequest.getUserId());
            Optional<ProductEntity> productEntity = productRepository.findById(createCommentRequest.getProductId());
            CommentEntity parentComment = null;
            if (createCommentRequest.getParentId() != null) {
                Optional<CommentEntity> commentEntity = commentRepository.findById(createCommentRequest.getParentId());
                if (commentEntity.isPresent()) {
                    parentComment = commentEntity.get();
                }
            }
            if (userEntity.isPresent() && productEntity.isPresent()) {
                UserEntity user = userEntity.get();
                ProductEntity product = productEntity.get();
                CommentEntity comment = new CommentEntity();
                comment.setParentComment(parentComment);
                comment.setUser(user);
                comment.setProduct(product);
                comment.setContent(createCommentRequest.getContent());
//              Lưu comment
                comment = commentRepository.save(comment);
//                CommentResponse response = mapper.map(comment,CommentResponse.class);
//
//                //      Trả về Json
//                ResponseData<Object> data = ResponseData.builder().data(response).build();
                return ResponseEntity.ok().body(ResponseObject
                        .builder()
                        .status("SUCCESS")
                        .message("Create comment success!")
                        .results("")
                        .build());
            }
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message("Data not exist!")
                            .results("")
                            .build()
                    );
        } catch (Exception e) {
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

    @Override
    public ResponseEntity<?> getListCommentByProduct(Long productId) {
        try {
                Optional<ProductEntity> productEntity = productRepository.findById(productId);
                if(productEntity.isPresent()) {
                    List<CommentDTO> commentEntities = getRootCommentsByProduct(productEntity.get());
                    ResponseData<Object> data = ResponseData.builder().data(commentEntities).build();
                    return ResponseEntity.ok().body(ResponseObject
                            .builder()
                            .status("SUCCESS")
                            .message("get list comment success!")
                            .results(data)
                            .build());
                }

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseObject.builder()
                            .status("FAIL")
                            .message(e.getMessage())
                            .results("")
                            .build()
                    );
        }
        return null;
    }


    @Transactional
    @Override
    public ResponseEntity<?> deleteCommentAndChildren(Long commentId) {
        try {
            commentRepository.findById(commentId).ifPresent(this::deleteCommentAndChildrenRecursive);
                return ResponseEntity.ok().body(ResponseObject
                        .builder()
                        .status("SUCCESS")
                        .message("get list comment success!")
                        .results("")
                        .build());

        } catch (Exception e) {
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

    private void deleteCommentAndChildrenRecursive(CommentEntity comment) {
        List<CommentEntity> childComments = comment.getChildComments();
        for (CommentEntity child : childComments) {
            deleteCommentAndChildrenRecursive(child);
        }
        commentRepository.delete(comment);
    }
    public CommentDTO convertToDTO(CommentEntity comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setUser(comment.getUser().getUserName());

        List<CommentDTO> childComments = new ArrayList<>();
        for (CommentEntity childComment : comment.getChildComments()) {
            CommentDTO childCommentDTO = convertToDTO(childComment);
            childComments.add(childCommentDTO);
        }
        commentDTO.setChildComments(childComments);

        return commentDTO;
    }
//lỗi
    public List<CommentDTO> getRootCommentsByProduct(ProductEntity product) {
        List<CommentEntity> rootComments = commentRepository.findRootCommentsByProduct(product);
        List<CommentDTO> organizedComments = new ArrayList<>();
        for (CommentEntity comment : rootComments) {
            CommentDTO commentDTO = convertToDTO(comment);
            organizedComments.add(commentDTO);
        }
        return organizedComments;
    }
}
