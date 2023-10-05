package com.shopee.clone.DTO.comment.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shopee.clone.DTO.auth.user.User;
import com.shopee.clone.DTO.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private Product product;
    private User user;
    private String content;
}
