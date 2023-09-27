package com.shopee.clone.DTO.product.update;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class SingleUpdateChangeImageProductItem {
    private Long imageProductItemId;
    private String imagePublicId;
    private MultipartFile file;
}
