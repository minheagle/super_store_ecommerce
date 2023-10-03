package com.shopee.clone.DTO.auth.user;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ChangeAvatarRequest {
    private String publicId;
    private MultipartFile avatar;
}
