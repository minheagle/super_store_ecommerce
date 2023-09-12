package com.shopee.clone.service.upload_cloud;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IUploadImageService {
    String uploadSingleAvatar(MultipartFile avatarImage) throws IOException;
    String uploadMultipleProductImage(MultipartFile productImages) throws IOException;
    String uploadMultipleCommentImage(MultipartFile commentImages) throws IOException;
    void deleteSingleImage(String publicId);
    void deleteMultipleImage(List<String> listPublicId);
}
