package com.shopee.clone.DTO.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {
    private Long id;
    private String content;
    private String imageUrl;
    private String imagePublicId;
    private Integer left;
    private Integer right;
    private Long parentId;
}
