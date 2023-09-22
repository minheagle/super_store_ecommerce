package com.shopee.clone.DTO.category.create;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class CreateCategory {
    private Long parentId;
    @NotBlank(message = "Required")
    private String content;
    private MultipartFile file;
}
