package com.shopee.clone.DTO.product_review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductReviewResponse {
    private Long id;
    private Long userId;
    private String fullName;
    private String imgAvatar;
    private int voteStar;
    private String comment;
    private List<String> imgURL;
}
