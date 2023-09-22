package com.shopee.clone.service.upload_cloud.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.shopee.clone.DTO.upload_file.ImageUploadResult;
import com.shopee.clone.config.CloudinaryConfig;
import com.shopee.clone.service.upload_cloud.IUploadImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UploadImageService implements IUploadImageService {
    private final Cloudinary cloudinary;

    public UploadImageService(Cloudinary cloudinary, CloudinaryConfig cloudinaryConfig) {
        this.cloudinary = cloudinary;
    }

    @Override
    public ImageUploadResult uploadSingle(MultipartFile file, String folder) throws IOException {
        Map uploadResult = cloudinary
                .uploader()
                .upload(file.getBytes(), ObjectUtils.asMap("folder", folder));
        return ImageUploadResult
                .builder()
                .public_id((String) uploadResult.get("public_id"))
                .secure_url((String) uploadResult.get("secure_url"))
                .build();
    }

    @Override
    public List<ImageUploadResult> uploadMultiple(MultipartFile[] files, String folder) throws IOException {
        return Arrays.stream(files).map(file -> {
            try {
                return uploadSingle(file, folder);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    @Override
    public ImageUploadResult replaceSingle(MultipartFile file, String existsPublicId, String folder) throws IOException {
        cloudinary.uploader().destroy(existsPublicId, null);
        Map uploadResult = cloudinary
                .uploader()
                .upload(file.getBytes(), ObjectUtils.asMap("folder", folder));
        return ImageUploadResult
                .builder()
                .public_id((String) uploadResult.get("public_id"))
                .secure_url((String) uploadResult.get("secure_url"))
                .build();
    }

    @Override
    public List<ImageUploadResult> replaceMultiple(MultipartFile[] files, List<String> existsPublicIdList) throws IOException {
        return null;
    }

    @Override
    public void deleteSingleImage(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, null);
    }

    @Override
    public void deleteMultipleImage(List<String> listPublicId) throws IOException {
        listPublicId.forEach(item -> {
            try {
                cloudinary.uploader().destroy(item, null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
