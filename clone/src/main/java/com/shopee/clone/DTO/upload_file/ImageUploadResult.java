package com.shopee.clone.DTO.upload_file;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageUploadResult {
    private String public_id;
    private String secure_url;
}
