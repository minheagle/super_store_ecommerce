package com.shopee.clone.DTO.product.update;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SingleUpdateChangeImageProductItem {
    private Long imageProductItemId;
    private String imagePublicId;
    private MultipartFile file;
}
