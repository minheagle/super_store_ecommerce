package com.shopee.clone.DTO.category.update;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class UpdateCategory {
    private Long id;
    private String content;
    private MultipartFile file;
}
