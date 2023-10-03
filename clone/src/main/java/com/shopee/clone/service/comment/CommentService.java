package com.shopee.clone.service.comment;

import com.shopee.clone.DTO.comment.request.CreateCommentRequest;
import org.springframework.http.ResponseEntity;

public interface CommentService {
    ResponseEntity<?> createComment(CreateCommentRequest createCommentRequest);
}
