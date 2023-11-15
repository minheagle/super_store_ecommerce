package com.shopee.clone.rest_controller.controller_public.comment;

import com.shopee.clone.service.comment.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/public/comments")
public class CommentPublicRestController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/{productId}")
    public ResponseEntity<?> getListCommentByProduct(@PathVariable Long productId){
        return commentService.getListCommentByProduct(productId);
    }
}
