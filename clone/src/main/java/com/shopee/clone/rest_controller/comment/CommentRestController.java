package com.shopee.clone.rest_controller.comment;

import com.shopee.clone.DTO.comment.request.CreateCommentRequest;
import com.shopee.clone.service.cart.CartService;
import com.shopee.clone.service.comment.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment")
public class CommentRestController {
    @Autowired
    private CommentService commentService;
    @PostMapping("/create")
    public ResponseEntity<?> createComment(@RequestBody CreateCommentRequest createCommentRequest){
        return commentService.createComment(createCommentRequest);
    }
    @GetMapping("/{productId}")
    public ResponseEntity<?> getListCommentByProduct(@PathVariable Long productId){
        return commentService.getListCommentByProduct(productId);
    }
    @PostMapping("/delete/{commentId}")
    public ResponseEntity<?> deleteComments(@PathVariable Long commentId){
        return commentService.deleteCommentAndChildren(commentId);
    }
}
