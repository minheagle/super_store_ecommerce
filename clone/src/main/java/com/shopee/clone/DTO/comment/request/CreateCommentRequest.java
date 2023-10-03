package com.shopee.clone.DTO.comment.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommentRequest {
    private Long userId;
    private Long productId;
    private String content;
    private Long parentId;
}
