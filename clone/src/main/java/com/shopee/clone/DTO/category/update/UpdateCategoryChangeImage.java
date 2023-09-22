package com.shopee.clone.DTO.category.update;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class UpdateCategoryChangeImage {
    private Long id;
    private String content;
    private String imagePublicId;
    private MultipartFile file;
}
