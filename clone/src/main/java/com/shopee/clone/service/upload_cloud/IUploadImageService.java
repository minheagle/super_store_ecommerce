package com.shopee.clone.service.upload_cloud;

import com.shopee.clone.DTO.upload_file.ImageUploadResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
public interface IUploadImageService {
    ImageUploadResult uploadSingle(MultipartFile file, String folder) throws IOException;
    List<ImageUploadResult> uploadMultiple(MultipartFile[] files, String folder) throws IOException;
    ImageUploadResult replaceSingle(MultipartFile file, String existsPublicId, String folder) throws IOException;
    List<ImageUploadResult> replaceMultiple(MultipartFile[] files, List<String> existsPublicIdList) throws IOException;
    void deleteSingleImage(String publicId) throws IOException;
    void deleteMultipleImage(List<String> listPublicId) throws IOException;
}
