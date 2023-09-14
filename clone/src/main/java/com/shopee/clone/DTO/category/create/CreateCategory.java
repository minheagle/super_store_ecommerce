package com.shopee.clone.DTO.category.create;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class CreateCategory {
    private String content;
    private MultipartFile file;
}
